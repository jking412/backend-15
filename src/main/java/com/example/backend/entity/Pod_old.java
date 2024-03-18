package com.example.backend.entity;

import io.kubernetes.client.openapi.models.V1Pod;
import lombok.Data;

// 我希望这是一个数据库实体，为了前期方便处理，我将V1Pod作为其成员
// 后期应该将V1Pod的字段拆分到数据库中
@Data
public class Pod_old {

    private V1Pod v1Pod;

    private int podId;

    public Pod_old(){
        v1Pod = new V1Pod();
    }

    public Pod_old(V1Pod v1Pod, int podId){
        this.v1Pod = v1Pod;
        this.podId = podId;
    }

}
