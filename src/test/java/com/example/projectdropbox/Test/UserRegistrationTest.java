package com.example.projectdropbox.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.projectdropbox.controllers.UserController;
import com.example.projectdropbox.repositories.UserRepository;
import com.example.projectdropbox.services.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class UserRegistrationTest {

    @Test
    public void testUserRegistration() throws Exception {
        // Mocka UserService och UserRepository
        UserService userService = mock(UserService.class);
        UserRepository userRepository = mock(UserRepository.class);

        // Skapa en instans av UserController med mockade tjänster
        UserController userController = new UserController(mock(AuthenticationProvider.class), userService);

        // Ange användardata för testet
        String username = "testuser";
        String password = "testpassword";

        // Mocka UserService för att simulera att användarregistreringen lyckades
        when(userService.registerUser(eq(username), eq(password))).thenReturn(createMockUserDetails());

        // Kör testet
        String result = userController.register(username, password);

        // Verifiera resultatet
        assertEquals("User registered successfully!", result);
    }

    // Hjälpmetod för att skapa ett mock UserDetailsService
    private UserDetails createMockUserDetails() {
        return new org.springframework.security.core.userdetails.User("testuser", "testpassword", Collections.emptyList());
    }
}
