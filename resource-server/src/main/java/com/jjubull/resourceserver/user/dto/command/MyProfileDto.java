package com.jjubull.resourceserver.user.dto.command;

import com.jjubull.common.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyProfileDto {

    private String username;
    private String nickname;
    private Grade grade;
    private String phone;
}
