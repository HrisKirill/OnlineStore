package com.khrystoforov.onlinestore.user.service.impl;

import com.khrystoforov.onlinestore.user.model.User;
import com.khrystoforov.onlinestore.user.repository.UserRepository;
import com.khrystoforov.onlinestore.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        log.info("Create user {}", user);
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        log.info("Get user by id {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User not found with id %d", id)));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user by id {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Exist user by email {}", email);
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Get user by email {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User not found with email %s", email)));
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return getUserByEmail(auth.getName());
    }
}
