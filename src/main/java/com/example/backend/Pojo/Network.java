package com.example.backend.Pojo;

import com.example.backend.k3s.SecurityGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Network {
    private Integer id;
    private String owner;
    private String name;
    private String description;
    private SecurityGroup securityGroup;
    private String createTime;
}
