package com.example.backend.Pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Disk {
    private Integer id;
    private String name;
    private String owner;
    private String hostPath;
    private String createTime;
}
