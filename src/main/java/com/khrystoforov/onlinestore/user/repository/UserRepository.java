package com.khrystoforov.onlinestore.user.repository;

import com.khrystoforov.onlinestore.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
