package com.example.backend.dao;


import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ConstantDaoImpl implements ConstantDao{

    Map<String,Object> constantMap;

    public ConstantDaoImpl() {
        constantMap = new HashMap<>();
        constantMap.put("maxContainerNum",10);
        constantMap.put("containerNameFormat","%s-%d");
        // 获取podName的正则表达式
        constantMap.put("podNameRegex","\\w+");
        // 获取PodId的正则表达式
        constantMap.put("podIdRegex","\\d+");
    }

    @Override
    public Object get(String key) {
        boolean b = constantMap.containsKey(key);
        if (b){
            return constantMap.get(key);
        }
        return null;
    }
}
