package com.company.librarymanagementsystem.controller;


import com.company.librarymanagementsystem.dto.LoginDto;
import com.company.librarymanagementsystem.dto.RegisterDto;
import com.company.librarymanagementsystem.security.JwtTokenProvider;
import com.company.librarymanagementsystem.service.impl.CustomUserDetailsService;
import com.company.librarymanagementsystem.service.inter.AuthServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

//  private final AuthenticationManager authenticationManager;
//
//  private final CustomUserDetailsService userDetailsService;
//
//  private final AuthServiceInter authServiceInter;
//
//  private final JwtTokenProvider jwtTokenProvider;
//
//  @Value("${jwtSecret}")
//  String jwtSecret;
//
//  @PostMapping("/login")
//  public String login(@ModelAttribute LoginDto loginRequest) {
//    Authentication authentication =
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUsername(),
//                            loginRequest.getPassword()
//                    )
//            );
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//    jwtTokenProvider.generateToken(authentication);
//    return "login";
//
//  }
//
//  @PostMapping("/register")
//  public String register(@ModelAttribute RegisterDto registerDto) {
//    authServiceInter.register(registerDto);
//    return "register";
//  }
}
