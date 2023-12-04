package com.example.projectdropbox.security;

import com.example.projectdropbox.JWTVerifyFilter;
import com.example.projectdropbox.repositories.UserRepository;
import com.example.projectdropbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, UserDetailsService userDetailsService)
            throws Exception {
        security
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(new JWTVerifyFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/test").hasRole("ADMIN")
                        .requestMatchers("/new").authenticated()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll());

        return security.build();
    }

    @Bean
    public AuthenticationProvider authProvider(
            UserDetailsService userService,
            PasswordEncoder encoder) {
        var dao = new DaoAuthenticationProvider();

        dao.setUserDetailsService(userService);
        dao.setPasswordEncoder(encoder);

        return dao;
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new UserService(userRepository, encoder);
    }

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

