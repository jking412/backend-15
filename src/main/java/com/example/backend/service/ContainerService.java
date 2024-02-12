package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerService {

    @Autowired
    UosService uosService;

    public void create(String podName,String hostName ,int cpuReq,int cpuLimit,int memoryReq,int memoryLimit){

    }
}
