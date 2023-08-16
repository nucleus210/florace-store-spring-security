package com.nucleus.floracestore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
@Slf4j
@Component
public class MyCustomDSL extends AbstractHttpConfigurer<MyCustomDSL, HttpSecurity> {
    private  final JwtConfig jwtConfiguration;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private  final SecretKey secretKey;
    private boolean flag;


    @Autowired
    public MyCustomDSL(JwtConfig jwtConfiguration, JwtAuthenticationFilter jwtAuthFilter, SecretKey secretKey) {
        this.jwtConfiguration = jwtConfiguration;
        this.jwtAuthFilter = jwtAuthFilter;
        this.secretKey = secretKey;
    }

    @Override
    public void configure(HttpSecurity http) {
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = context.getBean(JwtAuthenticationFilter.class);

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("MyCustomDSL: {}", authenticationManager);

    }


}