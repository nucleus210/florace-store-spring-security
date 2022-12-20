package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.UserNotFoundException;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository,
                                ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

//        if (userOptional.isEmpty()) {
//            log.error("User with name " + username + " not found!");
//            throw new UsernameNotFoundException("User with name " + username + " not found!");
//        }
//        log.info("Found user with name " + userOptional.get().getUsername());
        return new MyUserPrincipal(modelMapper.map(user, UserServiceModel.class));
    }

}