package com.example.backend.k3s;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class K3sService {

    private String serviceName;
    private Map<String,String> selector;
    private List<K3sServicePort> ports;
    private String type;

    public static String ClusterIP = "ClusterIP";
    public static String NodePort = "NodePort";
    public static String LoadBalancer = "LoadBalancer";

    public K3sService(){
        this.selector = new HashMap<>();
        this.ports = new ArrayList<>();
    }

    public void create(CoreV1Api api,String namespace) throws ApiException {

        V1Service service = new V1Service();
        service.setApiVersion("v1");
        service.setKind("Service");

        V1ObjectMeta meta = new V1ObjectMeta();
        meta.setName(serviceName);
        service.setMetadata(meta);

        List<V1ServicePort> ports = new ArrayList<>();
        for(K3sServicePort port : this.ports){
            ports.add(new V1ServicePort()
                    .port(port.getPort())
                    .targetPort(port.getTargetPort())
                    .protocol(port.getProtocol())
                    // TODO: 与SecurityGroupPort的name并无关联，但是这并不好,需要将SecurityGroupPort和K3sServicePort整合
                    // 需要将protocol小写，因为k8s的api规定protocol必须小写
                    .name(String.format("%d-%s", port.getPort(), port.getProtocol().toLowerCase()))
            );
        }

        V1ServiceSpec spec = new V1ServiceSpec();
        spec.setSelector(selector);
        spec.setPorts(ports);
        spec.setType(type);
        service.setSpec(spec);

        // create k3s service
        api.createNamespacedService(namespace, service, null, null, null,null);
    }

    public void delete(CoreV1Api api,String namespace) throws ApiException {
        api.deleteNamespacedService(serviceName,namespace,null,null,null,null,null,null);
    }
}
