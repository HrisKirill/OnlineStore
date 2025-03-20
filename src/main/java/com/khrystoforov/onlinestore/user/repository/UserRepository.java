package com.khrystoforov.onlinestore.user.repository;

import com.khrystoforov.onlinestore.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);
}
