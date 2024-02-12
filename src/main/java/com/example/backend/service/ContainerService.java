package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerService {

    @Autowired
    UosService uosService;

    public static String uosImage = "uos";

    public int create(String imageName,String podName,String hostName ,int cpuReq,int cpuLimit,int memoryReq,int memoryLimit)
    throws Exception{
        if (imageName.equals(uosImage)){
            return uosService.create(podName,hostName,cpuReq,cpuLimit,memoryReq,memoryLimit);
        }
        return -1;
    }

    public void delete(String imageName,int num) throws Exception{
        if (imageName.equals(uosImage)){
            uosService.delete(num);
        }
    }


}
