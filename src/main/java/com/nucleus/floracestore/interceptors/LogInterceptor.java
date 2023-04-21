package com.nucleus.floracestore.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {
    private Logger LOG = LoggerFactory.getLogger(LogInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LOG.info("preHandle method called....{}:{}"  + request.getRequestURI(), request.getMethod() );
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LOG.info("postHandle method called....{}:{}"  + request.getRequestURI(), request.getMethod() );
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(ex!=null) {
            LOG.error("Exception inside afterCompletion " + ex.getMessage());
        }

        LOG.info("afterCompletion method called....{}:{}"  + request.getRequestURI(), request.getMethod() );

    }
}
