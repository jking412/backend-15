package com.example.backend.mapper;

import com.example.backend.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface LoginMapper {

    @Select("select * from user where account=#{account} and password=#{password}")
    User login1(User user);

}
