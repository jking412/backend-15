package com.example.backend.service;

import com.example.backend.k3s.K3sPod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodService {

    @Autowired
    UosService uosService;

    public static String uosImage = "uos";

    public int create(K3sPod k3sPod) throws Exception{
        if (k3sPod.getImageName().equals(uosImage)){
            return uosService.create(k3sPod);
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
