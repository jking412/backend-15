package com.example.backend.k3s;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SecurityGroup {

    private String name;
    private String description;
    private List<SecurityGroupPort> ports;

    public SecurityGroup(String name, String description){
        this.name = name;
        this.description = description;
        this.ports = new ArrayList<>();
    }

    public void addPort(int port, String protocol){
        this.ports.add(new SecurityGroupPort(port, protocol));
    }
}
