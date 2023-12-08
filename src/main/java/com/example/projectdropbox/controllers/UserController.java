package com.example.projectdropbox.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.projectdropbox.UsernameAlreadyExistsException;
import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.services.FolderService;
import com.example.projectdropbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private AuthenticationProvider authProvider;
    private final UserService userService;
    @Autowired
    public UserController(AuthenticationProvider authProvider, UserService userService) {
        this.authProvider = authProvider;
        this.userService = userService;;
    }
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        try {
            userService.registerUser(username, password);
            return "User registered successfully!";
        } catch (UsernameAlreadyExistsException e) {
            return "Username already exists. Please choose another username.";
        }
    }
    @PostMapping("/login")
    public String login(@RequestHeader String username, @RequestHeader String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        var result = authProvider.authenticate(auth);

        if (result.isAuthenticated()) {
            var algoritm = Algorithm.HMAC256("secret code");
            var token = JWT.create()
                    .withSubject(username)
                    .withIssuer("auth0")
                    .sign(algoritm);

            return token;
        }

        return "Failed to login.";
    }
}


