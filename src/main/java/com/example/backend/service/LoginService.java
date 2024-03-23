package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.Pojo.User;
import com.example.backend.entity.UserInfo;
import com.example.backend.k3s.K3sPod;
import com.example.backend.mapper.LoginMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    @Resource
    private LoginMapper loginMapper;

    public UserInfo login1(UserInfo user) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getEmail,user.getEmail());
        wrapper.eq(UserInfo::getPassword, user.getPassword());
        return loginMapper.selectOne(wrapper);
    }

    public void register(UserInfo user) {
        UserInfo u = new UserInfo();
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        loginMapper.insert(u);

    }
}


