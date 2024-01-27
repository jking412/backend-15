package com.example.backend.controller;

import com.example.backend.service.UosService;
import com.google.protobuf.Api;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/env")
public class UosController {

    @Autowired
    private UosService uosService;

    @GetMapping("/create")
    public Map<Object,Object> create() throws Exception {
        int res = uosService.create();
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
        List<Integer> list = uosService.list();
        for(int i = 0 ; i < list.size() ; i++){
            boolean res = uosService.delete(list.get(i).intValue());
            if(!res){
                return Map.of("code",500,"msg","删除失败");
            }
        }
        return Map.of("code",200,"msg","删除成功");
    }

}
