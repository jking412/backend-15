package com.example.backend.k3s;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import lombok.Data;

import java.util.ArrayList;
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
}
