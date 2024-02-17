package com.example.backend.dao;


import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class Constant implements ConstantDao{

    Map<String,Object> constantMap;

    public Constant() {
        constantMap = new HashMap<>();
        constantMap.put("maxContainerNum",10);
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
