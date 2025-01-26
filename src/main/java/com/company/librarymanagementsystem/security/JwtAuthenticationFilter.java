package com.company.librarymanagementsystem.security;

import com.company.librarymanagementsystem.service.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter{
//
//  private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//  @Autowired
//  private final JwtTokenProvider tokenProvider;
//
//  @Autowired
//  private final CustomUserDetailsService customUserDetailsService;
//
//  public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
//    this.tokenProvider = tokenProvider;
//    this.customUserDetailsService = customUserDetailsService;
//  }
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//          FilterChain filterChain) throws ServletException, IOException {
//    log.info("Checking jwt token...");
//    try {
//      String jwt = getJwtFromRequest(request);
//
//      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//        Long userId = tokenProvider.getUserIdFromJWT(jwt);
//
//        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
//
//        UsernamePasswordAuthenticationToken authentication = new
//                UsernamePasswordAuthenticationToken(userDetails, null,
//                userDetails.getAuthorities());
//        authentication.setDetails(new
//                WebAuthenticationDetailsSource().buildDetails(request));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);
//      }
//    } catch (Exception ex) {
//      log.error("Could not set user authentication in security context", ex);
//    }
//
//    filterChain.doFilter(request, response);
//  }
//
//  private String getJwtFromRequest(HttpServletRequest request) {
//    String bearerToken = request.getHeader("Authorization");
//    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//      return bearerToken.substring(7, bearerToken.length());
//    }
//    return null;
//  }
}
