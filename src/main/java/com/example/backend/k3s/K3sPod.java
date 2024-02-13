package com.example.backend.k3s;


import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class K3sPod {

    private int podId;
    private int cpuReq;
    private int cpuLimit;
    private int memoryReq;
    private int memoryLimit;
    private String passwd;
    private String podName;
    private String hostName;
    private String containerName;
    private String imageName;
    private String imagePullPolicy;
    private Map<String,String> labels;
    private List<Integer> ports;
    // volumeName: volumePath
    private Map<String,String> volumes;
    // volumeName: mountPath
    private Map<String,String> volumeMounts;
    static String defaultPolicy = "IfNotPresent";

    public K3sPod(){
        imagePullPolicy = defaultPolicy;

        this.labels = new HashMap<>();
        this.volumes = new HashMap<>();
        this.volumeMounts = new HashMap<>();

    }

    public K3sPod(V1Pod v1pod){

        V1Container v1podContainer = v1pod.getSpec().getContainers().get(0);
        podId = Integer.parseInt(v1podContainer.getName().substring(4));
        podName = v1pod.getMetadata().getName();

        V1ResourceRequirements resources = v1podContainer.getResources();
        if (resources.getRequests() != null){
            for(Map.Entry<String, Quantity> entry : resources.getRequests().entrySet()){
                if (entry.getKey().equals("cpu")){
                    cpuReq = entry.getValue().getNumber().intValue();
                }
                if (entry.getKey().equals("memory")){
                    memoryReq = entry.getValue().getNumber().divide(new BigDecimal(1024*1024)).intValue();
                }
            }
        }


        if (resources.getLimits() != null){
            for(Map.Entry<String, Quantity> entry : resources.getLimits().entrySet()){
                if (entry.getKey().equals("cpu")){
                    cpuLimit = entry.getValue().getNumber().intValue();
                }
                if (entry.getKey().equals("memory")){
                    memoryLimit = entry.getValue().getNumber().divide(new BigDecimal(1024*1024)).intValue();
                }
            }
        }

        containerName = v1podContainer.getName();
        imageName = v1podContainer.getImage();
        imagePullPolicy = v1podContainer.getImagePullPolicy();

//        labels = v1pod.getMetadata().getLabels();
//        ports = new ArrayList<>();
//        for(V1ContainerPort port : v1podContainer.getPorts()){
//            ports.add(port.getContainerPort());
//        }
//
//        volumes = new HashMap<>();
//        volumeMounts = new HashMap<>();
//        for(V1Volume volume : v1pod.getSpec().getVolumes()){
//            volumes.put(volume.getName(),volume.getHostPath().getPath());
//        }
//
//        for(V1VolumeMount volumeMount : v1podContainer.getVolumeMounts()){
//            volumeMounts.put(volumeMount.getName(),volumeMount.getMountPath());
//        }

    }

    public K3sPod(V1Pod v1pod,String passwd){
        this(v1pod);
        this.passwd = passwd;
    }

    // String imageName,String podName,String hostName ,int cpuReq,int cpuLimit,int memoryReq,int memoryLimit
    public K3sPod(String imageName,String podName,String passwd,String hostName ,int cpuReq,int cpuLimit,int memoryReq,int memoryLimit){
        this();
        this.imageName = imageName;
        this.podName = podName;
        this.passwd = passwd;
        this.hostName = hostName;
        this.cpuReq = cpuReq;
        this.cpuLimit = cpuLimit;
        this.memoryReq = memoryReq;
        this.memoryLimit = memoryLimit;
    }

    public void create(CoreV1Api api,String namespace) throws ApiException{

        // create pod object
        V1Pod pod = new V1Pod();
        pod.setApiVersion("v1");
        pod.setKind("Pod");

        // create metadata
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName(podName);
        meta.setLabels(labels);
        pod.setMetadata(meta);

        // create container port
        List<V1ContainerPort> ports = new ArrayList<>();
        for(Integer port : this.ports){
            ports.add(new V1ContainerPort()
                    .containerPort(port)
            );
        }

        // create container
        V1Container container = new V1Container();
        container.setName(containerName);
        container.setImage(imageName);
        container.setImagePullPolicy(imagePullPolicy);
        container.setPorts(ports);

        // create resource requirements
        V1ResourceRequirements requirements = new V1ResourceRequirements();
        if (cpuReq != 0 || memoryReq != 0){
            Map<String, Quantity> requests = new HashMap<>();
            if (cpuReq != 0){
                requests.put("cpu",new Quantity(String.format("%d",cpuReq)));
            }
            if (memoryReq != 0){
                requests.put("memory",new Quantity(String.format("%dMi",memoryReq)));
            }
            requirements.setRequests(requests);
        }

        // create resource limits
        if (cpuLimit != 0 || memoryLimit != 0){
            Map<String, Quantity> limits = new HashMap<>();
            if (cpuLimit != 0){
                limits.put("cpu",new Quantity(String.format("%d",cpuLimit)));
            }
            if (memoryLimit != 0){
                limits.put("memory",new Quantity(String.format("%dMi",memoryLimit)));
            }
            requirements.setLimits(limits);
        }

        container.setResources(requirements);

        // create env
        if (passwd != null && !passwd.equals("")){
            List<V1EnvVar> env = new ArrayList<>();
            env.add(new V1EnvVar()
                    .name("VNC_PW")
                    .value(passwd)
            );
            container.setEnv(env);
        }


        // create volume mounts
        if (volumeMounts != null){
            List<V1VolumeMount> volumeMounts = new ArrayList<>();
            for(Map.Entry<String,String> entry : this.volumeMounts.entrySet()){
                volumeMounts.add(new V1VolumeMount()
                        .name(entry.getKey())
                        .mountPath(entry.getValue())
                );
            }
            container.setVolumeMounts(volumeMounts);
        }


        // create pod spec
        V1PodSpec spec = new V1PodSpec();
        spec.addContainersItem(container);

        // create volume
        if (volumes != null){
            List<V1Volume> volumes = new ArrayList<>();
            for(Map.Entry<String,String> entry : this.volumes.entrySet()){
                volumes.add(new V1Volume()
                        .name(entry.getKey())
                        .hostPath(new V1HostPathVolumeSource()
                                .path(entry.getValue())
                        )
                );
            }
            spec.setVolumes(volumes);
        }

        // set pod spec
        pod.setSpec(spec);

        // create k3s pod
        api.createNamespacedPod(namespace, pod, null, null, null,null);
    }

    public void delete(CoreV1Api api,String namespace) throws ApiException {
        api.deleteNamespacedPod(podName,namespace,null,null,null,null,null,null);
    }

    // pod 会在gracePeriodSeconds后被强制删除
    public void deleteForce(CoreV1Api api,String namespace,Integer gracePeriodSeconds) throws ApiException {
        api.deleteNamespacedPod(podName,namespace,null,null,gracePeriodSeconds,null,null,null);
    }

}
