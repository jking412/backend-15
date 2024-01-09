package com.example.backend.k3s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
public class K3s {

    private CoreV1Api coreV1Api;

    public K3s() {
        try {
            ApiClient client = Config.fromConfig("/home/jking/.kube/config");
            Configuration.setDefaultApiClient(client);
        }catch (IOException e){
            System.out.println("kube config read error");
            return;
        }

        coreV1Api = new CoreV1Api();
    }

    public V1PodList listPod() throws ApiException {
        return coreV1Api.listPodForAllNamespaces(null, null, null,
                null, null, null, null,
                null, null, null);

    }
}
