package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kunkun
 * @since 2024-03-20
 */
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    private String username;

    private String email;

    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "userId = " + userId +
            ", username = " + username +
            ", email = " + email +
            ", password = " + password +
        "}";
    }
}
