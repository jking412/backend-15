package com.example.backend.Pojo;

import com.example.backend.k3s.disk.Disk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class K3sPodConfig {
    private String imageName;
    private String name;
    private String passwd;
    private String hostName;
    private Integer cpuReq;
    private Integer cpuLimit;
    private Integer memoryReq;
    private Integer memoryLimit;
}
