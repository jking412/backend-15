package com.example.backend.controller;

import com.example.backend.entity.Result;
import com.example.backend.service.DiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/disk")
public class DiskController {
    @Autowired
    private DiskService diskService;
    @ApiOperation(value = "创建磁盘", notes = "创建磁盘")
    @PostMapping()
    public Result disk(@RequestBody Map<String,String> map) throws Exception {
        String name = map.get("name");
        String podName = map.get("podName");
        String podPath = map.get("podPath");
        com.example.backend.k3s.disk.Disk disk = new com.example.backend.k3s.disk.Disk(name,podName,podPath);
        boolean res = diskService.create(disk);
        if (res){
            return Result.success(200,Map.of("VolumeId",name,"podName",podName,"podPath",podPath));
        }
        return Result.error(500,"创建失败");
    }


    @ApiOperation(value = "删除磁盘", notes = "删除磁盘")
    @DeleteMapping()
    public Result diskDelete(@RequestParam String name) throws Exception {
        com.example.backend.k3s.disk.Disk disk = new com.example.backend.k3s.disk.Disk();
        disk.setName(name);
        boolean res = diskService.delete(disk);
        if (res){
            return Result.success(204,"删除成功");
        }
        return Result.error(500,"删除失败");
    }

    @ApiOperation(value = "获取所有磁盘", notes = "获取所有磁盘")
    @GetMapping()
    public Result getAllDisks() throws Exception {
        return Result.success(200, diskService.listAllVolumes());
    }

    @ApiOperation(value = "获取磁盘", notes = "获取磁盘")
    @GetMapping("/getDisk")
    public Result getDisk(@RequestParam String name) throws Exception {
        return Result.success(200, diskService.getDisk(name));
    }

    //更新磁盘
    @ApiOperation(value = "更新磁盘", notes = "更新磁盘")
    @PutMapping()
    public Result updateDisk(@RequestBody Map<String,String> map) throws Exception {
        String name = map.get("name");
        String podName = map.get("podName");
        String podPath = map.get("podPath");
        com.example.backend.k3s.disk.Disk disk = new com.example.backend.k3s.disk.Disk(name,podName,podPath);
        boolean res = diskService.update(disk);
        if (res){
            return Result.success(200,Map.of("VolumeId",name,"podName",podName,"podPath",podPath));
        }
        return Result.error(500,"更新失败");
    }

}
