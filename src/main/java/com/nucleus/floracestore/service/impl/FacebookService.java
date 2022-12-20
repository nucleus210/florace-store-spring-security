package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.InternalServerException;
import com.nucleus.floracestore.model.facebook.FacebookUser;
import com.nucleus.floracestore.model.service.UserRegistrationServiceModel;
import com.nucleus.floracestore.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
public class FacebookService {
    private final FacebookClient facebookClient;
    private FacebookUser facebookUser;
    private final UserServiceSocialImpl userServiceSocialImpl;
    private final RoleService roleService;
    private final JwtTokenProvider tokenProvider;
    @Autowired
    public FacebookService(FacebookClient facebookClient,
                           FacebookUser facebookUser,
                           UserServiceSocialImpl userServiceSocialImpl,
                           RoleService roleService,
                           JwtTokenProvider tokenProvider) {

        this.facebookClient = facebookClient;
        this.facebookUser = facebookUser;
        this.userServiceSocialImpl = userServiceSocialImpl;
        this.roleService = roleService;
        this.tokenProvider = tokenProvider;
    }

    public String loginUser(String fbAccessToken) {
        facebookUser = facebookClient.getUser(fbAccessToken);


        return userServiceSocialImpl.findById(facebookUser.getId())
                .or(() -> Optional.ofNullable(userServiceSocialImpl.registerUser(convertTo(facebookUser))))
                .map(MyUserPrincipal::new)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .map(tokenProvider::generateToken)
                .orElseThrow(() ->
                        new InternalServerException("unable to login facebook user id " + facebookUser.getId()));
    }

    private UserRegistrationServiceModel convertTo(FacebookUser facebookUser) {
        return UserRegistrationServiceModel.builder()
                .email(facebookUser.getEmail())
                .username(generateUsername(facebookUser.getFirstName(), facebookUser.getLastName()))
                .password(generatePassword(8))
                .roles(Set.of(roleService.getByRoleName("FACEBOOK_USER")))
                .build();
    }

    private String generateUsername(String firstName, String lastName) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%s.%s.%06d", firstName, lastName, number);
    }

    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }
}