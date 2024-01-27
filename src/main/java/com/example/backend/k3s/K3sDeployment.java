package com.example.backend.k3s;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class K3sDeployment {

    private String deploymentName;
    private Map<String,String> matchLabels;
    private int replicas;

    public K3sDeployment(){
        matchLabels = new HashMap<>();
    }

    public void create(AppsV1Api api, String namespace, V1PodTemplateSpec podTemplateSpec) throws ApiException {
        // create deployment object
        var deployment = new io.kubernetes.client.openapi.models.V1Deployment();
        deployment.setApiVersion("apps/v1");
        deployment.setKind("Deployment");

        // create metadata
        var meta = new io.kubernetes.client.openapi.models.V1ObjectMeta();
        meta.setName(deploymentName);
        deployment.setMetadata(meta);

        // create spec
        var spec = new io.kubernetes.client.openapi.models.V1DeploymentSpec();
        spec.setReplicas(replicas);

        // create selector
        var selector = new io.kubernetes.client.openapi.models.V1LabelSelector();
        selector.setMatchLabels(matchLabels);
        spec.setSelector(selector);

        // create template
        spec.setTemplate(podTemplateSpec);
        deployment.setSpec(spec);

        api.createNamespacedDeployment(namespace,deployment,null,null,null,null);
    }

    public void delete(AppsV1Api api,String namespace) throws ApiException {
        api.deleteNamespacedDeployment(deploymentName,namespace,null,null,null,null,null,null);
    }

}
