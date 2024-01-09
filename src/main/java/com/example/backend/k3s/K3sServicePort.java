package com.example.backend.k3s;

import io.kubernetes.client.custom.IntOrString;
import lombok.Data;

@Data
public class K3sServicePort {

    private Integer port;
    private IntOrString targetPort;
    private String protocol;

    public K3sServicePort() {

    }

    public K3sServicePort(Integer port, IntOrString targetPort, String protocol) {
        this.port = port;
        this.targetPort = targetPort;
        this.protocol = protocol;
    }

}
