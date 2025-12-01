package com.jjubull.resourceserver.user.repository;

import com.jjubull.common.domain.Provider;
import com.jjubull.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByProviderAndSub(Provider provider, String sub);
}
