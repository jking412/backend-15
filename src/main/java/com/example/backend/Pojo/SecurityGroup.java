package com.example.backend.Pojo;

import com.example.backend.k3s.SecurityGroupPort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityGroup {
    private Integer securityGroupId;
    private String name;
    private String description;
    private List<SecurityGroupPort> ports;
}
