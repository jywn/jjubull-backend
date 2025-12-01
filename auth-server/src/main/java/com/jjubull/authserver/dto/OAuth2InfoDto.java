package com.jjubull.authserver.dto;

import com.jjubull.common.domain.Provider;
import lombok.Data;

@Data
public class OAuth2InfoDto {

    private Provider provider;
    private String sub;

    public OAuth2InfoDto(Provider provider, String sub) {
        this.provider = provider;
        this.sub = sub;
    }
}
