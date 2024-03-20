package com.example.backend.k3s;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Service
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
//        Network network = new Network();

        // create k3s service
        api.createNamespacedService(namespace, service, null, null, null,null);
    }

    public void delete(CoreV1Api api,String namespace) throws ApiException {
        api.listNamespacedService(namespace, null, null, null, null, null, null, null, null, null, null).getItems().forEach(service -> {
            if (service.getMetadata().getName().equals(serviceName)) {
                try {
                    api.deleteNamespacedService(serviceName, namespace, null, null, null, null, null, null);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List list(CoreV1Api api,String namespace) throws ApiException {
        List result = new ArrayList();
        api.listNamespacedService(namespace, null, null, null, null, null, null, null, null, null, null).getItems().forEach(service -> {
            Map<String,Object> utilMap = new HashMap<>();
            Map<String,String> utilMap2 = new HashMap<>();
            utilMap2.put("Name", service.getMetadata().getName());
            utilMap2.put("Labels", String.valueOf(service.getMetadata().getNamespace()));
            utilMap2.put("CreateTime", String.valueOf(service.getMetadata().getCreationTimestamp()));
            utilMap2.put("ClusterIP", String.valueOf(service.getSpec().getClusterIP()));
            utilMap2.put("Port", String.valueOf(service.getSpec().getPorts().get(0).getPort()));
            utilMap2.put("TargetPort", String.valueOf(service.getSpec().getPorts().get(0).getTargetPort()));
            utilMap.put(service.getMetadata().getName(), utilMap2 );
            result.add(utilMap);
        });
        return result;
    }

    public Network getNetwork(String name, CoreV1Api api) {
//        Network network = new Network();
//        try {
//            V1Service service = api.readNamespacedService(name, "default", null);
//            network.setServiceName(service.getMetadata().getName());
//            network.setClusterIp(service.getSpec().getClusterIP());
//            network.setTargetPort(String.valueOf(service.getSpec().getPorts().get(0).getTargetPort()));
//            network.setCreateTime(service.getMetadata().getCreationTimestamp().toLocalDateTime());
//            network.setPort(String.valueOf(service.getSpec().getPorts().get(0).getPort()));
//            network.setLabels(String.valueOf(service.getMetadata().getNamespace()));
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
        return null;
    }
}
