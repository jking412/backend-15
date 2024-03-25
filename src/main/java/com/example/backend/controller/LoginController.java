package com.example.backend.controller;

import com.example.backend.Pojo.Result;
import com.example.backend.entity.UserInfo;
import com.example.backend.service.LoginService;
import com.example.backend.utils.JwtUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@Tag(name = "登录", description = "登录/登出")
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody UserInfo user) {
        log.info("...{}", user);

        UserInfo u = loginService.login1(user);
        //登陆成功，生成令牌，下发令牌
        if (u != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id",user.getUserId());
            claims.put("uEmail", user.getEmail());
            claims.put("uPwd", user.getPassword());
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(200, jwt);
        } else {
            loginService.register(user);
            return Result.success(200,"注册成功");
        }
    }


    @PostMapping("/logout")
    public Result logout(){

        return Result.success();
    }
}
