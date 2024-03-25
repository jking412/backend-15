package com.example.backend.controller;

import com.example.backend.entity.Result;
import com.example.backend.k3s.SecurityGroup;
import com.example.backend.service.NetworkService;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/network")
public class NetworkController {
    @Autowired
    NetworkService networkService;


    @ApiOperation(value = "创建网络", notes = "创建网络")
    @PostMapping()
    public Result network(@RequestBody Map<String,Object> map) throws Exception {
        String name = (String) map.get("name");
        int podId = Integer.parseInt((String) map.get("podId"));
        String podImage = (String) map.get("podImage");
        // 提取ports
        List<Map<String,String>> ports = (List<Map<String, String>>) map.get("ports");
        SecurityGroup securityGroup = new SecurityGroup(name,"");
        for(Map<String,String> port : ports){
            securityGroup.addPort(Integer.parseInt(port.get("port")),port.get("protocol"));
        }
        com.example.backend.k3s.Network network = new com.example.backend.k3s.Network(name,securityGroup,podId,podImage);
        networkService.create(network);
        //insert into databaes

        return Result.success(200,"创建成功");
    }

    @ApiOperation(value = "删除网络", notes = "删除网络")
    @DeleteMapping()
    public Result networkDelete(@RequestParam String name) throws Exception {
        networkService.delete(name);
        return Result.success(204,"删除成功");
    }

    @ApiOperation(value = "获取所有网络", notes = "获取所有网络")
    @GetMapping()
    public Result getAllNetworks() throws Exception {
        return Result.success(200, networkService.listNetworksFromDatabase());
    }
}

