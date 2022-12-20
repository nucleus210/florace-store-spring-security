package com.nucleus.floracestore.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class QueryRuntimeAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(QueryRuntimeException.class)
    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setErrorMessage(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

    }
//    public void springHandleNotFound(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.NOT_FOUND.value());
//    }
}
