package com.company.librarymanagementsystem.config;

import com.company.librarymanagementsystem.security.JwtAuthenticationEntryPoint;
import com.company.librarymanagementsystem.security.JwtAuthenticationFilter;
import com.company.librarymanagementsystem.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(
//        jsr250Enabled = true,
//        securedEnabled = true,
//        prePostEnabled = true
//)
public class SecurityConfig {

//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final CustomUserDetailsService userDetailsService;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
//                          CustomUserDetailsService userDetailsService,
//                          JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

//    @Bean
//    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder=
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//
//        var authManager=authenticationManagerBuilder.build();
//        http.authenticationManager(authManager);
//        http.csrf(AbstractHttpConfigurer::disable)
//                .cors(cors-> cors.configurationSource(request -> {
//                    var corsConfiguration=new CorsConfiguration();
//                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
//                    corsConfiguration.setAllowedMethods(List.of("POST","GET","DELETE","PUT","OPTIONS"));
//                    corsConfiguration.setAllowedHeaders(List.of("*"));
//                    corsConfiguration.setAllowCredentials(true);
//                    return corsConfiguration;
//                }))
//                .authorizeHttpRequests(auth->auth
//                                .requestMatchers("/**").permitAll()
////                        .requestMatchers("/login", "/register").permitAll()
////                        .requestMatchers("/admin/**").hasRole("ADMIN")
////                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
////                        .requestMatchers("/content/**").hasAuthority("VISITOR")
////                        .anyRequest().authenticated()
//                )
//                .sessionManagement(managment->managment
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic(basic->basic.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//                .exceptionHandling(Customizer.withDefaults())
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
}
