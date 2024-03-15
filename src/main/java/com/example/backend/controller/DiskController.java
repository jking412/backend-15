package com.example.backend.controller;

import com.example.backend.Pojo.Result;
import com.example.backend.service.DiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/disk")
public class DiskController {
    @Autowired
    private DiskService diskService;
    @PostMapping()
    public Result disk(@RequestBody Map<String,String> map) throws Exception {
        String name = map.get("name");
        String podName = map.get("podName");
        String podPath = map.get("podPath");
        com.example.backend.k3s.disk.Disk disk = new com.example.backend.k3s.disk.Disk(name,podName,podPath);
        boolean res = diskService.create(disk);
        if (res){
            return Result.success(200,"创建成功");
        }
        return Result.error(500,"创建失败");
    }

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

    @GetMapping()
    public Result getAllDisks() throws Exception {
        return Result.success(200, diskService.getAllDisks());
    }

}
