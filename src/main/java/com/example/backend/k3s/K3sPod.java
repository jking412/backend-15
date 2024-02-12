package com.example.backend.k3s;


import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.Data;

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
    private String podName;
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
}
