package com.nucleus.floracestore.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogUsernameAop {
    private  Authentication auth;
    @Before("@annotation(LogUsername)")
    public String logUsername() throws Throwable {
        System.out.println(auth.getName());
        return auth.getName();
    }

}