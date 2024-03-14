package com.example.backend.Pojo;

import lombok.Data;

@Data
public class Disk {
    private Integer id;
    private String name;
    private String owner;
    private String hostPath;
    private String createTime;
}
