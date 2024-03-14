package com.example.backend.Pojo;

import lombok.Data;

@Data
public class securityGroupPort {
    private Integer securityGroupId;
    private String name;
    private int port;
    private String protocol;
}
