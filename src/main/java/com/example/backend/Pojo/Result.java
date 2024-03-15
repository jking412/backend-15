package com.example.backend.Pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg; //响应信息 描述字符串
    private Object data; //返回的数据

    //增删改 成功响应
    public static Result success(){
        return new Result(200,"success",null);
    }
    //查询 成功响应
    public static Result success(Integer code,Object data){
        return new Result(code,"success",data);
    }
    //失败响应
    public static Result error(Integer code,String msg){
        return new Result(code,msg,null);
    }
}