package com.example.backend.controller;

import com.example.backend.entity.Result;
import com.example.backend.k3s.K3sPod;
import com.example.backend.service.PodService;
import com.example.backend.service.UosService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/container")
public class ContainerController {
    @Autowired
    private UosService uosService;

    @Autowired
    private PodService podService;


    @ApiOperation("创建容器")
    @PostMapping()
    // 获取body中的podName）
    public Result create(@RequestBody com.example.backend.entity.K3sPodConfig podConfig) throws Exception {

        // 以下为测试，仅供参考
//        Disk disk = new Disk("disk-1","uos","/home/test");
//        disk.setHostPath(diskService.getDiskPath(disk));
//
//        Map<String,Disk> mountDisks = Map.of("/test",disk);

        K3sPod k3sPod = new K3sPod(podConfig);
        int res = uosService.create(k3sPod);
        int i =podService.insert(k3sPod);
        if (res == -1 && i == 0){
            return Result.error(500,"创建失败，已达到最大数量");
        }
        return Result.success(201,String.format("/env/%d/vnc.html?resize=remote&path=env/%d/websockify",res,res));
    }

    // /delete/{num}
    @ApiOperation("根据id删除单个容器")
    @DeleteMapping()
    public Result delete(@RequestParam int num) throws Exception {
        boolean res = uosService.delete(num);
        int res1 = podService.deleteInDatabase(num);

        if (res && res1 == 1){
            return Result.success(204, "删除成功");
        }
        return Result.error(500,"删除失败");
    }

    @ApiOperation("获取所有容器")
    @GetMapping()
    public Result list() throws Exception {
        return Result.success(200,podService.listAll());
    }

    @GetMapping("/get")
    @ApiOperation("获取指定id的容器")
    public Result get(@RequestParam int id) throws Exception {
        return Result.success(200,podService.get(id));
    }

    @ApiOperation("删除所有容器")
    @DeleteMapping("/deleteAll")
    public Result deleteAll() throws Exception {
        List< K3sPod > list = uosService.list();
        for (K3sPod k3sPod : list) {
            boolean res = uosService.delete(k3sPod.getPodId());
            boolean res1 = podService.deleteAllInDatabase();
            if (!res && !res1){

                return Result.error(500, "删除失败");
            }
        }
        return Result.success(204,"删除成功");
    }

//    @ApiOperation("更新容器")
//    @PutMapping()
//    public Result update(@RequestBody com.example.backend.entity.K3sPodConfig podConfig) throws Exception {
//        K3sPod k3sPod = new K3sPod(podConfig);
//        boolean res = uosService.updatePod(k3sPod);
//        boolean res1 = podService.updateInDatabase(k3sPod);
//        if (res && res1){
//            return Result.success(200,"更新成功");
//        }
//        return Result.error(500,"更新失败");
//    }

}
