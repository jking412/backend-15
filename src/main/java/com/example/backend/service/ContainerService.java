package com.example.backend.service;

import com.example.backend.k3s.K3sPod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean delete(String imageName,int num) throws Exception{
        if (imageName.equals(uosImage)){
            return uosService.delete(num);
        }
        return false;
    }

    public List<K3sPod> list() throws Exception{
        return uosService.list();
    }

}
