package com.example.backend;

import com.example.backend.k3s.*;
import com.example.backend.utils.NginxConf;
import io.kubernetes.client.custom.IntOrString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class K3sTests {

    @Test
    void k3sTest() throws Exception {
        K3s k3s = new K3s();
        // uos pod
        K3sPod pod = new K3sPod();
        pod.setPodName("uos");
        pod.setContainerName("uos");
        pod.setImageName("uos:v0.1.0");
        pod.setLabels(Map.of("app","uos"));
        pod.setPorts(List.of(6080));
        pod.create(k3s.getCoreV1Api(),"default");

        // uos pod 2
        K3sPod pod2 = new K3sPod();
        pod2.setPodName("uos-2");
        pod2.setContainerName("uos-2");
        pod2.setImageName("uos:v0.1.0");
        pod2.setLabels(Map.of("app","uos-2"));
        pod2.setPorts(List.of(6080));
        pod2.create(k3s.getCoreV1Api(),"default");

        // uos service
        K3sService service = new K3sService();
        service.setServiceName("uos-svc");
        service.setType(K3sService.NodePort);
        service.setSelector(pod.getLabels());
        service.setPorts(List.of(new K3sServicePort(80,new IntOrString(6080),"TCP")));
        service.create(k3s.getCoreV1Api(),"default");

        // uos service 2
        K3sService service2 = new K3sService();
        service2.setServiceName("uos-svc-2");
        service2.setType(K3sService.NodePort);
        service2.setSelector(pod2.getLabels());
        service2.setPorts(List.of(new K3sServicePort(80,new IntOrString(6080),"TCP")));
        service2.create(k3s.getCoreV1Api(),"default");

        // nginx conf
        NginxConf nginxConf = new NginxConf();
        nginxConf.addUpstream("desktop","uos-svc:80");
        nginxConf.addLocationPrefix("desktop","/env/1");
        nginxConf.addUpstream("desktop-2","uos-svc-2:80");
        nginxConf.addLocationPrefix("desktop-2","/env/2");
        nginxConf.writeToNginxConfFile();

        // nginx pod
        K3sPod nginxPod = new K3sPod();
        nginxPod.setPodName("nginx");
        nginxPod.setContainerName("nginx");
        nginxPod.setImageName("nginx:alpine");
        nginxPod.setLabels(Map.of("app","ngx"));
        nginxPod.setPorts(List.of(80));
        nginxPod.setVolumes(Map.of("nginx-conf","/home/jking/IdeaProjects/backend/data/nginx/default.conf"));
        nginxPod.setVolumeMounts(Map.of("nginx-conf","/etc/nginx/conf.d/default.conf"));
        nginxPod.create(k3s.getCoreV1Api(),"default");

        // nginx service
        K3sService nginxService = new K3sService();
        nginxService.setServiceName("ngx-svc");
        nginxService.setType(K3sService.NodePort);
        nginxService.setSelector(nginxPod.getLabels());
        nginxService.setPorts(List.of(new K3sServicePort(80,new IntOrString(80),"TCP")));
        nginxService.create(k3s.getCoreV1Api(),"default");

        // ingress
        K3sIngress ingress = new K3sIngress();
        ingress.create(k3s.getNetworkingV1Api(),"default");
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
