package com.example.backend.service;


import com.example.backend.dao.ConstantDao;
import com.example.backend.dao.PodDao;
import com.example.backend.entity.Pod;
import com.example.backend.k3s.*;
import com.example.backend.utils.NginxConf;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@Slf4j
@Service
public class UosService {

    private Set<Integer> existService;

    private Lock lock;

    @Value("${k3s.max-container-num:0}")
    private int maxContainerNum;

    @Autowired
    private K3s k3s;

    @Autowired
    @Qualifier("constantDaoImpl")
    private ConstantDao constant;

    @Autowired
    @Qualifier("podDaoImpl")
    private PodDao podDao;

    private final String uosImageName = "uos";

    // TODO: 为了检查是否初始化，这里使用了一个flag，但是这样的实现方式不太好
    private boolean initFlag = false;




    public UosService()  {
        // TODO: 注入在构造函数中的存在问题，k3s不能使用Autowired注入
        existService = new HashSet<>();
        lock = new ReentrantLock();
    }

    private void init() throws Exception {

        // 加入在yaml配置文件中没有配置maxContainerNum，那么读取Dao层中的默认配置
        // maxContainerNum在大于0的情况下才被认为是有效的
        if(maxContainerNum <= 0){
            maxContainerNum = (int) constant.get("maxContainerNum");
        }
        log.info("maxContainerNum: {}",maxContainerNum);

//        V1PodList podList = k3s.listPod();
//        for (var item : podList.getItems()){
//            String containerName = item.getSpec().getContainers().get(0).getName();
//            if (containerName != null && containerName.startsWith("uos-")
//                    && item.getMetadata().getDeletionTimestamp() == null){
//                int num = Integer.parseInt(containerName.substring(4));
//                existService.add(num);
//            }
//        }

        List<Pod> podList = podDao.listPods(uosImageName);
        StringBuilder logStr = new StringBuilder("exist uos pod num list: [ ");
        for (var item : podList){
            existService.add(item.getPodId());
            logStr.append(item.getPodId()).append(" ");
        }
        logStr.append("]");
        log.info(logStr.toString());
    }
    public int create(K3sPod pod) throws Exception {
        if (!initFlag){
            init();
            initFlag = true;
        }
        lock.lock();
        try {
            return internalCreate(pod);
        }finally {
            lock.unlock();
        }
    }

    public boolean delete(int num) throws Exception{
        if (!initFlag){
            init();
            initFlag = true;
        }
        lock.lock();
        try {
            return internalDelete(num);
        }finally {
            lock.unlock();
        }
    }

    private int internalCreate(K3sPod k3sPod) throws Exception {

        if(existService.size() >= maxContainerNum){
            return -1;
        }

        int serviceNum;
        // 如果没有服务，编号为1，否则编号为最大编号+1
        if (existService.isEmpty()){
            serviceNum = new Random().nextInt(100);
        }else {
            int maxExistService = Collections.max(existService);
            serviceNum = new Random().nextInt(maxExistService + 100);
            // random 一个 不在existService中的编号
            while (existService.contains(serviceNum)){
                serviceNum = new Random().nextInt(maxExistService + 100);
            }
        }


        // uos pod
        K3sPod pod = new K3sPod();

        // 如果containerName不为空，就使用containerName，否则使用uos-编号
        if (k3sPod.getPodName() != null && !k3sPod.getPodName().isEmpty()){
            pod.setPodName(k3sPod.getPodName());
        }else {
            pod.setPodName(String.format("uos-%d",serviceNum));
        }

        pod.setContainerName(String.format("uos-%d",serviceNum));
        pod.setImageName("uos:v0.2.0");
        pod.setLabels(Map.of("app",String.format("uos-%d",serviceNum)));
        pod.setPorts(List.of(6080));

        // 设置资源
        pod.setCpuReq(k3sPod.getCpuReq());
        pod.setCpuLimit(k3sPod.getCpuLimit());
        pod.setMemoryReq(k3sPod.getMemoryReq());
        pod.setMemoryLimit(k3sPod.getMemoryLimit());

        // 设置密码
        pod.setPasswd(k3sPod.getPasswd());

        // 设置磁盘挂载
        if (k3sPod.getMountDisks() != null){
            pod.setMountDisks(k3sPod.getMountDisks());
        }

        pod.create(k3s.getCoreV1Api(),"default");

        // 在pod创建后，将编号加入existService
        existService.add(serviceNum);

        // uos service
        K3sService service = new K3sService();
        service.setServiceName(String.format("uos-svc-%d",serviceNum));
        service.setSelector(pod.getLabels());
        service.setPorts(List.of(new K3sServicePort(80,new IntOrString(6080),"TCP")));
        service.create(k3s.getCoreV1Api(),"default");

        // nginx conf
//        for (int num : existService){
//            nginxConf.addUpstream(String.format("desktop-%d",num),String.format("uos-svc-%d:80",num));
//            nginxConf.addLocationPrefix(String.format("desktop-%d",num),String.format("/env/%d",num));
//        }
//
//        boolean res = nginxConf.writeToNginxConfFile();
//        if (!res){
//            return -1;
//        }

        // if exist nginx pod, delete it
        V1PodList podList = k3s.listPod();
        for (var item : podList.getItems()){
            if (item.getMetadata().getName() != null && item.getMetadata().getName().startsWith("ngx")){
                K3sPod nginxPod = new K3sPod();
                nginxPod.setPodName(item.getMetadata().getName());
                nginxPod.deleteForce(k3s.getCoreV1Api(),"default",0);
                break;
            }
        }

        // create nginx pod
        V1Pod nginxPod = new V1Pod();
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName("ngx");
        meta.setLabels(Map.of("app","ngx"));
        nginxPod.setMetadata(meta);

        V1PodSpec podSpec = new V1PodSpec();

        // nginx container
        V1Container container = new V1Container().name("nginx")
                .image("nginx:alpine")
                .imagePullPolicy("IfNotPresent")
                .ports(List.of(new V1ContainerPort().containerPort(80)))
                .volumeMounts(List.of(new V1VolumeMount().name("nginx-conf").mountPath("/etc/nginx/conf.d")));

        podSpec.setContainers(List.of(container));
        // nginx init container
        File file = new File("conf-gen.sh");
        FileReader fileReader = new FileReader(file);
        char[] buffer = new char[(int) file.length()];
        fileReader.read(buffer);
        fileReader.close();
        String shell = new String(buffer);

        List<String> command = new ArrayList<>();
        command.add("sh");
        command.add("-c");
        command.add(shell);
        command.add("--");
        for (int num : existService){
            command.add(String.format("%d",num));
        }

        V1Container ngxInitContainer = new V1Container().name("ngx-init")
                .image("busybox")
                .imagePullPolicy("IfNotPresent")
                .volumeMounts(List.of(new V1VolumeMount().name("nginx-conf").mountPath("/etc/nginx/conf.d")));
        ngxInitContainer.setCommand(command);

        podSpec.setInitContainers(List.of(ngxInitContainer));

        // nginx volume
        podSpec.setVolumes(List.of(new V1Volume().name("nginx-conf").emptyDir(new V1EmptyDirVolumeSource())));

        podSpec.setContainers(List.of(container));
        nginxPod.setSpec(podSpec);

        k3s.getCoreV1Api().createNamespacedPod("default",nginxPod,null,null,null,null);

        if (existService.size() == 1){

            // nginx pod
//            K3sPod nginxPod = new K3sPod();
//            nginxPod.setPodName("nginx");
//            nginxPod.setContainerName("nginx");
//            nginxPod.setImageName("nginx:alpine");
//            nginxPod.setLabels(Map.of("app","ngx"));
//            nginxPod.setPorts(List.of(80));
//            nginxPod.setVolumes(Map.of("nginx-conf","/home/jking/IdeaProjects/backend/data/nginx/default.conf"));
//            nginxPod.setVolumeMounts(Map.of("nginx-conf","/etc/nginx/conf.d/default.conf"));
//            nginxPod.create(k3s.getCoreV1Api(),"default");
            // nginx deployment
//            K3sDeployment nginxDeployment = new K3sDeployment();
//            nginxDeployment.setDeploymentName("ngx-dep");
//            nginxDeployment.setReplicas(1);
//            nginxDeployment.setMatchLabels(Map.of("app","ngx"));
//
//            // nginx template
//            V1PodTemplateSpec templateSpec = new V1PodTemplateSpec();
//            V1ObjectMeta meta = new V1ObjectMeta();
//            meta.setLabels(Map.of("app","ngx"));
//            templateSpec.setMetadata(meta);

//            spec:
//            containers:
//            - name: ngx
//            image: nginx:alpine
//            imagePullPolicy: IfNotPresent
//            ports:
//            - containerPort: 80
//            volumeMounts:
//            - mountPath: /etc/nginx/conf.d
//            name: ngx-conf
//
//            initContainers:
//            - name: ngx-init
//            image: busybox
//            command: ['sh', '-c', 'shell']
//            volumeMounts:
//            - mountPath: /etc/nginx/conf.d
//            name: ngx-conf
//            volumes:
//            - name: ngx-conf
//            emptyDir: {}

//            V1PodSpec podSpec = new V1PodSpec();
//            podSpec.setContainers(List.of(new V1Container()
//                    .name("nginx")
//                    .image("nginx:alpine")
//                    .imagePullPolicy("IfNotPresent")
//                    .ports(List.of(new V1ContainerPort()
//                            .containerPort(80)
//                    ))
//                    .volumeMounts(List.of(new V1VolumeMount()
//                            .name("nginx-conf")
//                            .mountPath("/etc/nginx/conf.d/default.conf")
//                    ))
//            ));

            // get nginxFilePath and nginxFileName
//            String nginxFilePath = nginxConf.getNginxFilePath();
//            String nginxFileName = nginxConf.getNginxFileName();
//
//            String nginxHostPath = String.format("%s/%s/%s",System.getProperty("user.dir"),nginxFilePath,nginxFileName);
//
//
//
//            podSpec.setVolumes(List.of(new V1Volume()
//                    .name("nginx-conf")
//                    .hostPath(new V1HostPathVolumeSource()
//                            .path(nginxHostPath)
//                    )
//            ));

//            podSpec.setVolumes(List.of(new V1Volume()
//                    .name("nginx-conf")
//                    .emptyDir(new V1EmptyDirVolumeSource())
//            ));
//
//            V1Container ngxContainer = new V1Container().name("ngx")
//                    .image("nginx:alpine")
//                    .imagePullPolicy("IfNotPresent")
//                    .ports(List.of(new V1ContainerPort().containerPort(80)))
//                    .volumeMounts(List.of(new V1VolumeMount().name("nginx-conf").mountPath("/etc/nginx/conf.d")));
//            podSpec.setContainers(List.of(ngxContainer));

            // read core shell from conf-gen.sh
//            File file = new File("conf-gen.sh");
//            FileReader fileReader = new FileReader(file);
//            char[] buffer = new char[(int) file.length()];
//            fileReader.read(buffer);
//            fileReader.close();
//            String shell = new String(buffer);
//
//            List<String> command = new ArrayList<>();
//            command.add("sh");
//            command.add("-c");
//            command.add(shell);
//            command.add("--");
//            for (int num : existService){
//                command.add(String.format("%d",num));
//            }
//
//            V1Container ngxInitContainer = new V1Container().name("ngx-init")
//                    .image("busybox")
//                    .volumeMounts(List.of(new V1VolumeMount().name("nginx-conf").mountPath("/etc/nginx/conf.d")));
//            ngxInitContainer.setCommand(command);
//
//            podSpec.setInitContainers(List.of(ngxInitContainer));
//
//            templateSpec.setSpec(podSpec);
//
//            nginxDeployment.create(k3s.getAppsV1Api(),"default",templateSpec);

            // nginx service
            K3sService nginxService = new K3sService();
            nginxService.setServiceName("ngx-svc");
            nginxService.setSelector(Map.of("app","ngx"));
            nginxService.setPorts(List.of(new K3sServicePort(80,new IntOrString(80),"TCP")));
            nginxService.create(k3s.getCoreV1Api(),"default");

            // ingress
            K3sIngress ingress = new K3sIngress();
            ingress.create(k3s.getNetworkingV1Api(),"default");
        }

        return serviceNum;
    }

    private boolean internalDelete(int num) throws Exception {

        if (!existService.contains(num)){
            return false;
        }

        // uos pod
        K3sPod pod = new K3sPod();
        // 通过num获取podName
        List<K3sPod> list = list();
        for (var item : list){
            if (item.getPodId() == num) {
                pod.setPodName(item.getPodName());
                break;
            }
        }
        // deleteForce 会保证pod在3s内被删除
        pod.deleteForce(k3s.getCoreV1Api(),"default",0);

        // 在pod删除后，从existService中删除
        existService.remove(num);


        // uos service
        K3sService service = new K3sService();
        service.setServiceName(String.format("uos-svc-%d",num));
        service.delete(k3s.getCoreV1Api(),"default");

        // 删除pod时conf只需要删除upstream和location，但是不需要更改文件
//        nginxConf.deleteUpstream(String.format("desktop-%d",num));
//        nginxConf.deleteLocationPrefix(String.format("desktop-%d",num));

        // delete nginx pod to reload nginx conf
//        if (existService.size() > 1){
//            V1PodList podList = k3s.listPod();
//            for (var item : podList.getItems()){
//                if (item.getMetadata().getName() != null && item.getMetadata().getName().startsWith("ngx")){
//                    K3sPod nginxPod = new K3sPod();
//                    nginxPod.setPodName(item.getMetadata().getName());
//                    nginxPod.deleteForce(k3s.getCoreV1Api(),"default",0);
//                    break;
//                }
//            }
//        }

        if (existService.isEmpty()){
            // nginx pod
            K3sPod nginxPod = new K3sPod();
            nginxPod.setPodName("ngx");
            nginxPod.deleteForce(k3s.getCoreV1Api(),"default",0);

            // nginx service
            com.example.backend.k3s.K3sService nginxService = new com.example.backend.k3s.K3sService();
            nginxService.setServiceName("ngx-svc");
            nginxService.delete(k3s.getCoreV1Api(),"default");

            // ingress
            K3sIngress ingress = new K3sIngress();
            ingress.delete(k3s.getNetworkingV1Api(),"default");
        }

        return true;
    }

    public List<K3sPod> list() throws ApiException {
        V1PodList podList = k3s.listPod();
        List<K3sPod> list = new ArrayList<>();

        for (var item : podList.getItems()){
//            if (item.getMetadata().getName() != null && item.getMetadata().getName().startsWith("uos-") &&
//                item.getMetadata().getDeletionTimestamp() == null){
//                // get num
//                String podName = item.getMetadata().getName();
//                int num = Integer.parseInt(podName.substring(4));
//                list.add(num);
//            }

            String containerName = item.getSpec().getContainers().get(0).getName();
            if (containerName != null && containerName.startsWith("uos-") &&
                    item.getMetadata().getDeletionTimestamp() == null){
//                int num = Integer.parseInt(containerName.substring(4));
//                pod.setPodId(num);
//                String podName = item.getMetadata().getName();
//                pod.setPodName(podName);
                K3sPod pod = new K3sPod(item);
                list.add(pod);
            }
        }

        return list;
    }
}
