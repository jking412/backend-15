package com.example.backend.k3s;


import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import lombok.Data;

import java.util.Map;

@Data
public class K3sPod {
    private String podName;
    private String containerName;
    private String imageName;
    private String imagePullPolicy;
    private Map<String,String> labels;

    static String defaultPolicy = "IfNotPresent";

    public K3sPod(){
        imagePullPolicy = defaultPolicy;
    }

    public void create(CoreV1Api api,String namespace) throws ApiException{

        // create pod object
        V1Pod pod = new V1Pod();
        pod.setApiVersion("v1");
        pod.setKind("Pod");

        // create metadata
        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName(podName);
        pod.setMetadata(meta);

        // create pod spec
        V1PodSpec spec = new V1PodSpec();
        spec.addContainersItem(new V1Container()
                .name(containerName)
                .image(imageName)
                .imagePullPolicy(imagePullPolicy)
        );
        pod.setSpec(spec);

        // create k3s pod
        api.createNamespacedPod(namespace, pod, null, null, null,null);
    }
}
