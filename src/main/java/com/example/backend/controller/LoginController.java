package com.example.backend.controller;

import com.example.backend.domain.Result;
import com.example.backend.domain.User;
import com.example.backend.service.LoginService;
import com.example.backend.service.iml.LoginServiceIml;
import com.example.backend.utils.JwtUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
public class LoginController {
    @Resource
    private LoginServiceIml loginServiceIml;

    @PostMapping("/login")
    public Result login(@RequestBody User user)
    {
        log.info("...{}",user);

        User u = loginServiceIml.login1(user);
        log.info("。。。{}",u);
        //登陆成功，生成令牌，下发令牌
        if(u !=null)
        {
            Map<String, Object> claims = new HashMap<>();

            claims.put("uPhone",user.getAccount());
            claims.put("uPwd",user.getPwd());
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        }
        //登陆失败，返回错误信息
        return Result.error("NOT_LOGIN");
    }
    @PostMapping("/logout")
    public Result logout(){

        return Result.success();
    }
}
