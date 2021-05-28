package io.github.vm.patlego.iot.server.authentication;

public interface Authentication<T> {
    
    public T validate(String token);

    public String createToken(T token);
}
