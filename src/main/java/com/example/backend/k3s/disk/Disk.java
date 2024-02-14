package com.example.backend.k3s.disk;

import lombok.Data;

@Data
public class Disk {

    private String name;
    private String podName;
    private String podPath;
//    private String hostPath;

    public Disk(String name, String podName, String podPath) {
        this.name = name;
        this.podName = podName;
        this.podPath = podPath;
    }

}
