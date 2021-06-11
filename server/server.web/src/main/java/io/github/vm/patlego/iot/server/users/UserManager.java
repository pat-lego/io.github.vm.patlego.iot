package io.github.vm.patlego.iot.server.users;

public interface UserManager {

    public boolean userExists(String username);
    
    public User getUser(String username);

    public Permissions getPermissions(String username);
}
