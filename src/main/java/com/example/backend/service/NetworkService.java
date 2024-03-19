package com.example.backend.service;

import com.example.backend.k3s.K3s;
import com.example.backend.k3s.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NetworkService {


    @Autowired
    private K3s k3s;
    @Autowired
    private INetworkService networkService;
    public void create(Network network) throws Exception {
        network.create(k3s.getCoreV1Api(),"default");
    }

    public void delete(String networkName) throws Exception {
        Network network = new Network(networkName,null,0,"");
        network.delete(k3s.getCoreV1Api(),"default");
    }

    public List<Object> getAllNetworks() {
        List<Object> result = new ArrayList<>();
        Network network = new Network("",null,0,"","");
        try {
            result = network.getAllNetworks(k3s.getCoreV1Api(), "default");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insert(String name) {
        com.example.backend.entity.Network network = new com.example.backend.entity.Network();
        Network network2 = new Network("",null,0,"","");
        network = network2.getNetwork(name,k3s.getCoreV1Api());
        networkService.save(network);

    }
}
