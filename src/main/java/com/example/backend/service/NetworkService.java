package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.configure.Constants;
import com.example.backend.entity.NetworkInfo;
import com.example.backend.k3s.K3s;
import com.example.backend.k3s.Network;
import com.example.backend.mapper.NetworkInfoMapper;
import io.kubernetes.client.openapi.models.V1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NetworkService {


    @Autowired
    private K3s k3s;

    @Autowired
    private NetworkInfoMapper networkInfoMapper;

    public void create(Network network) throws Exception {
        network.create(k3s.getCoreV1Api(), Constants.DEFAULT);
        V1Service service = k3s.getCoreV1Api().readNamespacedService(network.getName(), Constants.DEFAULT, null);

        //insert into database
        //todo:添加安全组 和 podID
        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setNetworkName(network.getName());
        networkInfo.setPodId(network.getPodId());
        networkInfo.setClusterIp(service.getSpec().getClusterIP());
        networkInfo.setCreateTime(service.getMetadata().getCreationTimestamp().toLocalDateTime());
        networkInfoMapper.insert(networkInfo);
    }

    public void delete(String networkName) throws Exception {
        Network network = new Network(networkName,null,0,"");
        network.delete(k3s.getCoreV1Api(),Constants.DEFAULT);
        deleteInDatabase(networkName);
    }

    //获取所有网络
    public List<NetworkInfo> listNetworksFromDatabase() {
        return networkInfoMapper.selectList(null);
    }

    public List<Object> getAllNetworks() {
        List<Object> result = new ArrayList<>();
        Network network = new Network("",null,0,"","");
        try {
            result = network.getAllNetworks(k3s.getCoreV1Api(), Constants.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean insert(String name) {
        LambdaQueryWrapper<NetworkInfo>wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(NetworkInfo::getNetworkName,name);
        if(networkInfoMapper.selectOne(wrapper)!=null){
            return false;
        }
        return true;
    }
    public boolean deleteInDatabase(String name) {
        LambdaQueryWrapper<NetworkInfo>wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(NetworkInfo::getNetworkName,name);
        if(networkInfoMapper.delete(wrapper)==1){
            return true;
        }
        return false;
    }

    public List<NetworkInfo> listAll() {
        return networkInfoMapper.selectList(null);
    }
    public NetworkInfo getNetwork(String name) {
        LambdaQueryWrapper<NetworkInfo>wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(NetworkInfo::getNetworkName,name);
        return networkInfoMapper.selectOne(wrapper);
    }


}
