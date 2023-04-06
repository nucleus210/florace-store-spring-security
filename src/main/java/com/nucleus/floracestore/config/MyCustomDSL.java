package com.nucleus.floracestore.config;

import com.nucleus.floracestore.jwt.JwtConfiguration;
import com.nucleus.floracestore.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
@Slf4j
@Component
public class MyCustomDSL extends AbstractHttpConfigurer<MyCustomDSL, HttpSecurity> {
    private  final JwtConfiguration jwtConfiguration;
    private  final SecretKey secretKey;

    @Autowired
    public MyCustomDSL(JwtConfiguration jwtConfiguration, SecretKey secretKey) {
        this.jwtConfiguration = jwtConfiguration;
        this.secretKey =secretKey;
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter( new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager,  jwtConfiguration, secretKey));
        log.info("MyCustomDSL: {}", authenticationManager);
    }

}