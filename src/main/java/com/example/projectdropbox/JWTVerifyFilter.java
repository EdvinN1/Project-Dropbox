package com.example.projectdropbox;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTVerifyFilter extends OncePerRequestFilter {

    private UserDetailsService userService;

    public JWTVerifyFilter(UserDetailsService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var algorithm = Algorithm.HMAC256("secret code");
            var verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            var jwt = verifier.verify(authHeader);

            var user = this.userService.loadUserByUsername(jwt.getSubject());

            var auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (JWTVerificationException exception) {
            throw new IllegalStateException("Failed to authenticate");
        }
    }

}
