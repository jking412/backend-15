package com.example.backend.controller;

import com.example.backend.k3s.*;
import com.example.backend.k3s.disk.Disk;
import com.example.backend.mapper.PodInfoMapper;
import com.example.backend.service.DiskService;
import com.example.backend.service.NetworkService;
import com.example.backend.service.UosService;
import com.google.protobuf.Api;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/env")
public class UosController {

    @Autowired
    private UosService uosService;
    @Autowired
    private NetworkService networkService;
    @Autowired
    private DiskService diskService;


    @GetMapping("/create")
    // 获取body中的podName）
    public Map<Object,Object> create(@RequestBody Map<String, String> body) throws Exception {

        // 以下为测试，仅供参考
//        Disk disk = new Disk("disk-1","uos","/home/test");
//        disk.setHostPath(diskService.getDiskPath(disk));
//
//        Map<String,Disk> mountDisks = Map.of("/test",disk);

        K3sPod k3sPod = new K3sPod("uos",body.get("name"),body.get("passwd"),"", 0, 0, 0, 0);
        int res = uosService.create(k3sPod);
        if (res == -1){
            return Map.of("code",500,"msg","创建失败，已达到最大数量");
        }
        return Map.of("code",200,"msg",String.format("/env/%d/vnc.html?resize=remote&path=env/%d/websockify",res,res));
    }

    // /delete/{num}
    @GetMapping("/delete/{num}")
    public Map<Object,Object> delete(@PathVariable int num) throws Exception {
        boolean res = uosService.delete(num);
        if (res){
            return Map.of("code",200,"msg","删除成功");
        }
        return Map.of("code",500,"msg","删除失败");
    }

    @GetMapping("/list")
    public Map<Object,Object> list() throws ApiException{
        return Map.of("code",200,"msg",uosService.list());
    }

    @GetMapping("/deleteAll")
    public Map<Object,Object> deleteAll() throws Exception {
        List< K3sPod > list = uosService.list();
        for(int i = 0 ; i < list.size() ; i++){
            boolean res = uosService.delete(list.get(i).getPodId());
            if(!res){
                return Map.of("code",500,"msg","删除失败");
            }
        }
        return Map.of("code",200,"msg","删除成功");
    }

    //    {
//        "name": "test",
//            "podId": "22",
//            "podImage": "uos",
//            "ports":[
//        {
//            "port": "80",
//                "protocol":"TCP"
//        },
//        {
//            "port": "6080",
//                "protocol":"TCP"
//        }
//    ]
//    }

}