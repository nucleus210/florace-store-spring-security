package com.nucleus.floracestore.config;

import com.nucleus.floracestore.jwt.JwtConfiguration;
import com.nucleus.floracestore.model.facebook.FacebookUser;
import com.nucleus.floracestore.service.impl.FacebookClient;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationBeansConfig implements WebMvcConfigurer {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}

    @Bean
    public FacebookUser facebookUser() {return new FacebookUser();}

    @Bean
    public JwtConfiguration jwtConfiguration() {return new JwtConfiguration();}
}
