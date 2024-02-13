package com.example.backend.service;

import com.example.backend.k3s.K3s;
import com.example.backend.k3s.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetworkService {


    @Autowired
    private K3s k3s;
    public void create(Network network) throws Exception {
        network.create(k3s.getCoreV1Api(),"default");
    }

    public void delete(String networkName) throws Exception {
        Network network = new Network(networkName,null,0,"");
        network.delete(k3s.getCoreV1Api(),"default");
    }
}
