package com.nucleus.floracestore.config;

import com.nucleus.floracestore.model.facebook.FacebookUser;
import com.nucleus.floracestore.utils.ImageUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationBeansConfig  {



    @Bean
    public ModelMapper modelMapper() {
       return new ModelMapper();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FacebookUser facebookUser() {
        return new FacebookUser();
    }

//    @Bean
//    public JwtConfiguration jwtConfiguration() {
//        return new JwtConfiguration();
//    }
    @Bean
    public ImageUtils imageUtils() {
        return new ImageUtils();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
