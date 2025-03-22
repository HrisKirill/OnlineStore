package com.khrystoforov.onlinestore.user.service.impl;

import com.khrystoforov.onlinestore.user.model.User;
import com.khrystoforov.onlinestore.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.khrystoforov.onlinestore.util.EntityUtil.getTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext secCont;

    @Test
    void testCreateUser() {
        when(userRepository.save(getTestUser())).thenReturn(getTestUser());
        User result = userService.createUser(getTestUser());
        assertEquals(getTestUser().getId(), result.getId());
        assertEquals(getTestUser().getName(), result.getName());
        assertEquals(getTestUser().getEmail(), result.getEmail());
    }

    @Test
    void testGetUser() {
        Long id = getTestUser().getId();
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(getTestUser()));
        assertEquals(getTestUser(), userService.getUser(id));
    }

    @Test
    void testGetUserThrowEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                userService.getUser(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser() {
        Long id = getTestUser().getId();
        userService.deleteUser(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testExistsByEmail() {
        String email = getTestUser().getEmail();
        when(userRepository.existsUserByEmail(email)).thenReturn(true);
        assertTrue(userService.existsByEmail(email));
    }

    @Test
    void testGetUserByEmail() {
        String email = getTestUser().getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(getTestUser()));
        assertEquals(getTestUser(), userService.getUserByEmail(email));
    }

    @Test
    void testGetUserByEmailThrowEntityNotFoundException() {
        String email = "";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                userService.getUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetCurrentUser() {
        when(secCont.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(secCont);
        when(authentication.getName()).thenReturn(getTestUser().getEmail());
        when(userRepository.findByEmail(getTestUser().getEmail()))
                .thenReturn(Optional.of(getTestUser()));

        assertEquals(userService.getCurrentUser(), getTestUser());
    }
}