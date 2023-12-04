package com.example.projectdropbox.controllers;

import com.example.projectdropbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private AuthenticationProvider authProvider;
    private final UserService userService;
    @Autowired
    public TestController(AuthenticationProvider authProvider, UserService userService) {
        this.authProvider = authProvider;
        this.userService = userService;;
    }


    @GetMapping("/new")
    public String newEndpoint(@AuthenticationPrincipal User user) {
        return "Your username: " + user.getUsername();
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
