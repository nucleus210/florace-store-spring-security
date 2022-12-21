package com.nucleus.floracestore.error;

public class RestError extends RuntimeException {
    public RestError(String msg, String authentication_failed_at_controller_advice) {
        super(msg + ": " + authentication_failed_at_controller_advice);
    }
}
