package com.example.backend.Pojo;

import com.example.backend.k3s.SecurityGroupPort;
import lombok.Data;

import java.util.List;
@Data
public class securityGroup {
    private Integer securityGroupId;
    private String name;
    private String description;
    private List<SecurityGroupPort> ports;
}
