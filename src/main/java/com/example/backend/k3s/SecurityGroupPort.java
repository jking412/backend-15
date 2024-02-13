package com.example.backend.k3s;


import lombok.Data;

@Data
public class SecurityGroupPort {

    private String name;
    private int port;
    private String protocol;

    public SecurityGroupPort(int port, String protocol){
//        this.name = String.format("%d-%s", port, protocol);
        this.name = "";
        this.port = port;
        this.protocol = protocol;
    }

    public SecurityGroupPort(String name, int port, String protocol){
        this.name = name;
        this.port = port;
        this.protocol = protocol;
    }
}
