package com.example.backend.Pojo;

import com.example.backend.k3s.disk.Disk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pod {

    private int podId;
    private int cpuReq;
    private int cpuLimit;
    private int memoryReq;
    private int memoryLimit;
    private String passwd;
    private String podName;
    private String hostName;
    private String containerName;
    private String imageName;
    private String imagePullPolicy;
    private Map<String,String> labels;
    private List<Integer> ports;
    // mountPath:Disk
    private Map<String, Disk> mountDisks;
    private Integer diskId;
    // volumeName: volumePath
    // volume default hostPath
    // name:hostPath
    private Map<String,String> volumeMounts;
    static String defaultPolicy = "IfNotPresent";
    private String createTime;
    private String runningTime;
    private String status;
    private String createUser;
    private Integer networkId;

}
