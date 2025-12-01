package com.jjubull.authserver.repository;

import com.jjubull.authserver.domain.OAuth2User;
import com.jjubull.common.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OAuth2UserRepository extends JpaRepository<OAuth2User, Long> {
     Optional<OAuth2User> findOAuth2UserByProviderAndSub(Provider provider, String sub);

     Boolean existsByProviderAndSub(Provider provider, String sub);
}
