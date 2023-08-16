package com.nucleus.floracestore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nucleus.floracestore.error.EmailAlreadyExistsException;
import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.error.UsernameAlreadyExistsException;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.payloads.AuthenticationResponse;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.UserRepository;
import com.nucleus.floracestore.service.RoleService;
import com.nucleus.floracestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nucleus.floracestore.token.Token;
import com.nucleus.floracestore.repository.TokenRepository;
import com.nucleus.floracestore.token.TokenType;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final MyUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           RoleService roleService,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           UserRepository userRepository,
                           MyUserDetailsService userDetailsService,
                           TokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }



    @Override
    public AuthenticationResponse loginUser(String username, String password) {

        log.info("Login user / UserService" );
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var user = userDetailsService.loadUserByUsername(username);

        var jwtToken = tokenProvider.generateToken(authentication);
        var refreshToken = tokenProvider.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .tokenType(tokenProvider.getTokenType())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    @Override
    public UserServiceModel registerUser(UserRegistrationServiceModel user) {
        String password = user.getPassword();
        UserEntity userEntity = register(user);
        userEntity.setAccountCreatedDate(new Date());
        user.setRoles(new HashSet<>() {{
            add(roleService.getByRoleName("ROLE_USER"));
        }});
        return mapToService( userRepository.save(userEntity));
    }
    @Override
    public UserServiceModel registerFacebookUser(UserRegistrationServiceModel user) {
        UserEntity userEntity = register(user);
        userEntity.setAccountCreatedDate(new Date());
        user.setRoles(new HashSet<>() {{
            add(roleService.getByRoleName("FACEBOOK_USER"));
        }});
        return mapToService( userRepository.save(userEntity));
    }
    @Override
    public void updateUser(UserServiceModel serviceModel, String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with username " + username));
            userEntity.setUsername(serviceModel.getUsername());
            userEntity.setEmail(serviceModel.getEmail());
            userRepository.save(userEntity);
    }

    @Override
    public UserServiceModel deleteUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with id " + userId));
        userRepository.delete(userEntity);
        return mapToService(userEntity);
    }

    @Override
    public void updateUserById(Long userId, UserServiceModel userServiceModel) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with id " + userId));
        userEntity.setUsername(userServiceModel.getUsername());
        userEntity.setEmail(userServiceModel.getEmail());
        userRepository.save(userEntity);
    }

    @Override
    public UserServiceModel findById(Long id) {
        log.info("UserService: retrieving user {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with id " + id));
        return mapToService(userEntity);
    }
    @Override
    public Optional<UserServiceModel> findBySocialId(Long id) {
        log.info("UserService: retrieving user {}", id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with id " + id));
        return Optional.of(mapToService(userEntity));
    }
    @Override
    public UserServiceModel findByUsername(String username) {
        log.info("UserService: retrieving user {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with name " + username));
        return mapToService(user);
    }
    @Override
    public UserServiceModel findByEmailAddress(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user with email " + email));
        log.info("UserService: found user with email " + email);
        return mapToService(user);
    }
    @Override
    public List<UserServiceModel> findAll() {
        log.info("UserService: retrieving all users");
        return userRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }



    private UserEntity register(UserRegistrationServiceModel user) {
        log.info("registering user {}", user.getUsername());

        if (existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());
            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if (existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());
            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return modelMapper.map(user, UserEntity.class);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        username = tokenProvider.extractUsername(refreshToken);

        if (username != null) {
            var user = this.userDetailsService.loadUserByUsername(username);
            log.info("User: " + user.getUsername());
            log.info("RefreshTokeValid: " + tokenProvider.isTokenValid(refreshToken, user));

            if (tokenProvider.isTokenValid(refreshToken, user)) {
                revokeAllUserTokens(user);
                var accessToken = "Bearer " + tokenProvider.generateToken(user);
                log.info("New access_token: " + accessToken);
                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }

        return null;
    }
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


    public Boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    private UserServiceModel mapToService(UserEntity entity) {
        return modelMapper.map(entity, UserServiceModel.class);
    }
    private void saveUserToken(UserEntity user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}