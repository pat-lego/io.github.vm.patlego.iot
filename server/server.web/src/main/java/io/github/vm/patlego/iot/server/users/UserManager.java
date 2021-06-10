package io.github.vm.patlego.iot.server.users;

import java.util.List;

public interface UserManager {

    public boolean userExists(String username);
    
    public User getUser(String username);

    public List<User> getUsers();
}
