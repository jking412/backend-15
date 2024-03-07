package com.example.backend;

import com.example.backend.k3s.*;
import com.example.backend.utils.NginxConf;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.Attach;
import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.PodMetricsList;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
@SpringBootTest
public class K3sTests {

    @Test
    void k3sTest() throws Exception {

    }

    @Test
    void k3sDestroyTest() throws Exception {
        K3s k3s = new K3s();
        // uos pod
        K3sPod pod = new K3sPod();
        pod.setPodName("uos");
        pod.delete(k3s.getCoreV1Api(),"default");

        // uos pod 2
        K3sPod pod2 = new K3sPod();
        pod2.setPodName("uos-2");
        pod2.delete(k3s.getCoreV1Api(),"default");

        // uos service
        K3sService service = new K3sService();
        service.setServiceName("uos-svc");
        service.delete(k3s.getCoreV1Api(),"default");

        // uos service 2
        K3sService service2 = new K3sService();
        service2.setServiceName("uos-svc-2");
        service2.delete(k3s.getCoreV1Api(),"default");

        // nginx pod
        K3sPod nginxPod = new K3sPod();
        nginxPod.setPodName("nginx");
        nginxPod.delete(k3s.getCoreV1Api(),"default");

        // nginx service
        K3sService nginxService = new K3sService();
        nginxService.setServiceName("ngx-svc");
        nginxService.delete(k3s.getCoreV1Api(),"default");

        // ingress
        K3sIngress ingress = new K3sIngress();
        ingress.delete(k3s.getNetworkingV1Api(),"default");
    }
}
