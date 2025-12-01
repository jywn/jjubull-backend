package com.jjubull.authserver.dto;

import lombok.Data;

@Data
public class NewLocalUserDto {

    private String account;
    private String password;
    private String email;
    private String name;
}
