package com.nucleus.floracestore;

import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.repository.UserRepository;
import com.nucleus.floracestore.service.UserService;
import com.nucleus.floracestore.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class UserServiceTest {
    private UserEntity testUser;
    private UserRepository mockedUserRepository;
    @Before
    public void init() {
        this.testUser = new UserEntity() {{
            setUserId(1l);
            setUsername("admin");
            setPassword("123456");
        }};
        this.mockedUserRepository = Mockito.mock(UserRepository.class);
    }}