package com.example.backend.Pojo;

import com.example.backend.k3s.SecurityGroup;
import lombok.Data;

@Data
public class Network {
    private Integer id;
    private String owner;
    private String name;
    private String description;
    private SecurityGroup securityGroup;
    private String createTime;
}
