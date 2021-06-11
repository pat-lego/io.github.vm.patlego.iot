package io.github.vm.patlego.iot.server.users;

public class LoginUser implements User {

    private String username;
    private String password;

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    
}
