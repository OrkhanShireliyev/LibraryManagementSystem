package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.RegisterDto;
import com.company.librarymanagementsystem.model.User;
import com.company.librarymanagementsystem.repository.UserRepository;
import com.company.librarymanagementsystem.service.inter.AuthServiceInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl  {
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private PasswordEncoder passwordEncoder;
//
//  @Override
//  public void register(RegisterDto registerDto) {
//    User user = new User();
//    user.setUsername(registerDto.getUsername());
//    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//    userRepository.save(user);
//  }
}
