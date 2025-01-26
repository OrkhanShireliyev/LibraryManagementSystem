package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.model.User;
import com.company.librarymanagementsystem.repository.UserRepository;
import com.company.librarymanagementsystem.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  {
//  private final UserRepository userRepository;
//
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    User user = userRepository.findByUsername(username)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//    UserPrincipal userPrincipal = new UserPrincipal();
//    userPrincipal.setUsername(user.getUsername());
//    userPrincipal.setPassword(user.getPassword());
//    userPrincipal.setId(user.getId());
//    userPrincipal.setLocked(user.getLocked());
//    userPrincipal.setRole(user.getRole());
//
//    return userPrincipal;
//  }
//
//  public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
//    User user = userRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//    UserPrincipal userPrincipal = new UserPrincipal();
//    userPrincipal.setUsername(user.getUsername());
//    userPrincipal.setPassword(user.getPassword());
//    userPrincipal.setId(user.getId());
//    userPrincipal.setRole(user.getRole());
//    userPrincipal.setLocked(user.getLocked());
//
//    return userPrincipal;
//  }
}
