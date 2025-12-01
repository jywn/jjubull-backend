package com.jjubull.authserver.dto;

import com.jjubull.common.domain.Provider;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidatedMyTokenDto {

    Provider provider;
    String sub;

    private ValidatedMyTokenDto(Provider provider, String sub) {
        this.provider = provider;
        this.sub = sub;
    }

    public static ValidatedMyTokenDto create(Provider provider, String sub) {
        return new ValidatedMyTokenDto(provider, sub);
    }
}
