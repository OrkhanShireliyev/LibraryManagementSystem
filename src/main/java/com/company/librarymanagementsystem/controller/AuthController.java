package com.company.librarymanagementsystem.controller;


import com.company.librarymanagementsystem.dto.LoginDto;
import com.company.librarymanagementsystem.dto.RegisterDto;
import com.company.librarymanagementsystem.security.JwtTokenProvider;
import com.company.librarymanagementsystem.service.impl.CustomUserDetailsService;
import com.company.librarymanagementsystem.service.inter.AuthServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final AuthServiceInter authServiceInter;

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwtSecret}")
    String jwtSecret;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok().body(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        authServiceInter.register(registerDto);
        return ResponseEntity.ok().body("User registered successfully");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            boolean isValid = jwtTokenProvider.validateToken(token);
            if (isValid) {
                return ResponseEntity.ok().body(Map.of("status", "valid"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
