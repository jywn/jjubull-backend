package com.jjubull.authserver.dto;

import lombok.Data;

@Data
public class NewUserDto {
    private String jwt;
    private String username;
    private String nickname;
    private String phone;
}
