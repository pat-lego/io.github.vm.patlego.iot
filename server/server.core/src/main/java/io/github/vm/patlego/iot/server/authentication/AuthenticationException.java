package io.github.vm.patlego.iot.server.authentication;

public class AuthenticationException extends Exception {

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
    
}
