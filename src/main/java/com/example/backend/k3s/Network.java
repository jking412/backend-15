package com.example.backend.k3s;


import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Network {

    private String name;
    private String description;
    private SecurityGroup securityGroup;

    private int PodId;
    private String podImage;
    private String podLabel;

    // name + securityGroup + podId + podImage
    public Network(String name, SecurityGroup securityGroup, int PodId, String podImage){
        this.name = name;
        this.securityGroup = securityGroup;
        this.PodId = PodId;
        this.podImage = podImage;
        this.podLabel = String.format("%s-%s", podImage, PodId);
    }

    public Network(String name, SecurityGroup securityGroup, int PodId, String podImage, String podLabel){
        this.name = name;
        this.securityGroup = securityGroup;
        this.PodId = PodId;
        this.podImage = podImage;
        this.podLabel = podLabel;
    }

    public void create(CoreV1Api api, String namespace) throws Exception {
        K3sService k3sService = new K3sService();
        k3sService.setServiceName(name);
        k3sService.setSelector(new HashMap<>(){{
            put("app", podLabel);
        }});
        k3sService.setType(K3sService.ClusterIP);
        List<SecurityGroupPort> ports = securityGroup.getPorts();
        for(SecurityGroupPort port : ports){
            k3sService.getPorts().add(new K3sServicePort(port.getPort(), new IntOrString(port.getPort()), port.getProtocol()));
        }
        k3sService.create(api, namespace);
    }

    public void delete(CoreV1Api api, String namespace) throws Exception {
        K3sService k3sService = new K3sService();
        k3sService.setServiceName(name);
        k3sService.delete(api, namespace);
    }
}
