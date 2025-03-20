package com.khrystoforov.onlinestore.user.service;

import com.khrystoforov.onlinestore.user.model.User;

public interface UserService {

    User createUser(User user);

    User getUser(Long id);

    void deleteUser(Long id);

    boolean existsByEmail(String email);

    User getUserByEmail(String email);
}
