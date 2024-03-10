package com.example.backend.Pojo;

import lombok.Data;

@Data
public class User {
    private Integer userId;
    private Long password;
    private String userName;
    private Long phoneNumber;
    private String email;

}
