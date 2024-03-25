package com.example.backend.controller;

import com.example.backend.Pojo.Result;
import com.example.backend.k3s.K3sPod;
import com.example.backend.k3s.Network;
import com.example.backend.k3s.SecurityGroup;
import com.example.backend.k3s.disk.Disk;
import com.example.backend.service.DiskService;
import com.example.backend.service.NetworkService;
import com.example.backend.service.UosService;
import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.PodMetricsList;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/container")
public class ContainerController {
    @Autowired
    private UosService uosService;


    @PostMapping()
    // 获取body中的podName）
    public Result create(@RequestBody com.example.backend.Pojo.K3sPodConfig podConfig) throws Exception {

        // 以下为测试，仅供参考
//        Disk disk = new Disk("disk-1","uos","/home/test");
//        disk.setHostPath(diskService.getDiskPath(disk));
//
//        Map<String,Disk> mountDisks = Map.of("/test",disk);

        K3sPod k3sPod = new K3sPod(podConfig);
        int res = uosService.create(k3sPod);
        if (res == -1){
            return Result.error(500,"创建失败，已达到最大数量");
        }

        return Result.success(201,String.format("/env/%d/vnc.html?resize=remote&path=env/%d/websockify",res,res));
    }

    // /delete/{num}
    @DeleteMapping("/{num}")
    public Result delete(@PathVariable int num) throws Exception {
        boolean res = uosService.delete(num);
        if (res){
            return Result.success(204,"删除成功");
        }
        return Result.error(500,"删除失败");
    }

    @GetMapping()
    public Result list() throws ApiException {
        return Result.success(200,uosService.list());
    }

    @DeleteMapping("/deleteAll")
    public Result deleteAll() throws Exception {
        List< K3sPod > list = uosService.list();
        for(int i = 0 ; i < list.size() ; i++){
            boolean res = uosService.delete(list.get(i).getPodId());
            if(!res){
                return Result.error(500,"删除失败");
            }
        }
        return Result.success(204,"删除成功");
    }

}
