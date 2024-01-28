package com.example.backend.k3s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingApi;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
public class K3s {

    private CoreV1Api coreV1Api;
    private NetworkingV1Api networkingV1Api;
    private AppsV1Api appsV1Api;
    public K3s() {

        ApiClient client = null;

        try {
            client = Config.fromConfig("/home/jking/.kube/config");
        }catch (IOException e){
            try{
                client = Config.fromCluster();
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
            return;
        }

        Configuration.setDefaultApiClient(client);


        coreV1Api = new CoreV1Api();
        networkingV1Api = new NetworkingV1Api();
        appsV1Api = new AppsV1Api();
    }

    public V1PodList listPod() throws ApiException {
        return coreV1Api.listNamespacedPod("default",null,null,null,null,null,null,
                null,null,null,null);

    }
}
