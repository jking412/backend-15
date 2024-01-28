package com.example.backend.service;


import com.example.backend.k3s.*;
import com.example.backend.utils.NginxConf;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@Service
public class UosService {

    private Set<Integer> existService;

    private Lock lock;

    @Value("${k3s.max-container-num}")
    private int maxContainerNum;

    private K3s k3s;

    @Autowired
    private NginxConf nginxConf;




    public UosService() throws Exception {
        // TODO: 注入在构造函数中的存在问题，k3s不能使用Autowired注入
        k3s = new K3s();
        existService = new HashSet<>();
        lock = new ReentrantLock();

        V1PodList podList = k3s.listPod();
        for (var item : podList.getItems()){
            if (item.getMetadata().getName() != null && item.getMetadata().getName().startsWith("uos-") &&
                item.getMetadata().getDeletionTimestamp() == null){
                // get num
                String podName = item.getMetadata().getName();
                int num = Integer.parseInt(podName.substring(4));
                existService.add(num);
            }
        }

    }


    public int create() throws Exception{
        lock.lock();
        try {
            return internalCreate();
        }finally {
            lock.unlock();
        }
    }

    public boolean delete(int num) throws Exception{
        lock.lock();
        try {
            return internalDelete(num);
        }finally {
            lock.unlock();
        }
    }

    public int internalCreate() throws Exception {

        if(existService.size() >= maxContainerNum){
            return -1;
        }

        int serviceNum;
        // 如果没有服务，编号为1，否则编号为最大编号+1
        if (existService.isEmpty()){
            serviceNum = 1;
        }else {
            serviceNum = Collections.max(existService) + 1;
        }

        existService.add(serviceNum);

        // uos pod
        K3sPod pod = new K3sPod();
        pod.setPodName(String.format("uos-%d",serviceNum));
        pod.setContainerName(String.format("uos-%d",serviceNum));
        pod.setImageName("uos:v0.1.0");
        pod.setLabels(Map.of("app",String.format("uos-%d",serviceNum)));
        pod.setPorts(List.of(6080));
        pod.create(k3s.getCoreV1Api(),"default");

        // uos service
        com.example.backend.k3s.K3sService service = new com.example.backend.k3s.K3sService();
        service.setServiceName(String.format("uos-svc-%d",serviceNum));
        service.setType(com.example.backend.k3s.K3sService.NodePort);
        service.setSelector(pod.getLabels());
        service.setPorts(List.of(new K3sServicePort(80,new IntOrString(6080),"TCP")));
        service.create(k3s.getCoreV1Api(),"default");

        // nginx conf
        for (int num : existService){
            nginxConf.addUpstream(String.format("desktop-%d",num),String.format("uos-svc-%d:80",num));
            nginxConf.addLocationPrefix(String.format("desktop-%d",num),String.format("/env/%d",num));
        }
        nginxConf.writeToNginxConfFile();

        // delete nginx pod to reload nginx conf
        if (existService.size() > 1){
            V1PodList podList = k3s.listPod();
            for (var item : podList.getItems()){
                if (item.getMetadata().getName() != null && item.getMetadata().getName().startsWith("ngx")){
                    K3sPod nginxPod = new K3sPod();
                    nginxPod.setPodName(item.getMetadata().getName());
                    nginxPod.delete(k3s.getCoreV1Api(),"default");
                    break;
                }
            }
        }

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
            K3sDeployment nginxDeployment = new K3sDeployment();
            nginxDeployment.setDeploymentName("ngx-dep");
            nginxDeployment.setReplicas(1);
            nginxDeployment.setMatchLabels(Map.of("app","ngx"));

            // nginx template
            V1PodTemplateSpec templateSpec = new V1PodTemplateSpec();
            V1ObjectMeta meta = new V1ObjectMeta();
            meta.setLabels(Map.of("app","ngx"));
            templateSpec.setMetadata(meta);

            V1PodSpec podSpec = new V1PodSpec();
            podSpec.setContainers(List.of(new V1Container()
                    .name("nginx")
                    .image("nginx:alpine")
                    .imagePullPolicy("IfNotPresent")
                    .ports(List.of(new V1ContainerPort()
                            .containerPort(80)
                    ))
                    .volumeMounts(List.of(new V1VolumeMount()
                            .name("nginx-conf")
                            .mountPath("/etc/nginx/conf.d/default.conf")
                    ))
            ));
            podSpec.setVolumes(List.of(new V1Volume()
                    .name("nginx-conf")
                    .hostPath(new V1HostPathVolumeSource()
                            .path("/home/jking/IdeaProjects/backend/data/nginx/default.conf")
                    )
            ));
            templateSpec.setSpec(podSpec);

            nginxDeployment.create(k3s.getAppsV1Api(),"default",templateSpec);

            // nginx service
            K3sService nginxService = new K3sService();
            nginxService.setServiceName("ngx-svc");
            nginxService.setType(com.example.backend.k3s.K3sService.NodePort);
            nginxService.setSelector(Map.of("app","ngx"));
            nginxService.setPorts(List.of(new K3sServicePort(80,new IntOrString(80),"TCP")));
            nginxService.create(k3s.getCoreV1Api(),"default");

            // ingress
            K3sIngress ingress = new K3sIngress();
            ingress.create(k3s.getNetworkingV1Api(),"default");
        }

        return serviceNum;
    }

    public boolean internalDelete(int num) throws Exception {

        if (!existService.contains(num)){
            return false;
        }

        existService.remove(num);

        // uos pod
        K3sPod pod = new K3sPod();
        pod.setPodName(String.format("uos-%d",num));
        pod.delete(k3s.getCoreV1Api(),"default");

        // uos service
        K3sService service = new K3sService();
        service.setServiceName(String.format("uos-svc-%d",num));
        service.delete(k3s.getCoreV1Api(),"default");

        // 删除pod时conf只需要删除upstream和location，但是不需要更改文件
        nginxConf.deleteUpstream(String.format("desktop-%d",num));
        nginxConf.deleteLocationPrefix(String.format("desktop-%d",num));

        // delete nginx pod to reload nginx conf
        if (existService.size() > 1){
            V1PodList podList = k3s.listPod();
            for (var item : podList.getItems()){
                if (item.getMetadata().getName() != null && item.getMetadata().getName().startsWith("ngx")){
                    K3sPod nginxPod = new K3sPod();
                    nginxPod.setPodName(item.getMetadata().getName());
                    nginxPod.delete(k3s.getCoreV1Api(),"default");
                    break;
                }
            }
        }

        if (existService.size() == 0){
            // nginx pod
            K3sDeployment nginxDeployment = new K3sDeployment();
            nginxDeployment.setDeploymentName("ngx-dep");
            nginxDeployment.delete(k3s.getAppsV1Api(),"default");

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

    public List<Integer> list() throws ApiException {
        V1PodList podList = k3s.listPod();
        List<Integer> list = new ArrayList<>();

        for (var item : podList.getItems()){
            if (item.getMetadata().getName() != null && item.getMetadata().getName().startsWith("uos-") &&
                item.getMetadata().getDeletionTimestamp() == null){
                // get num
                String podName = item.getMetadata().getName();
                int num = Integer.parseInt(podName.substring(4));
                list.add(num);
            }
        }

        return list;
    }
}
