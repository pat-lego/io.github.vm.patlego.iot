package io.github.vm.patlego.iot.server.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SimpleUserManager implements UserManager {

    private Properties users;

    public SimpleUserManager() throws IOException {
        this.users = new Properties();
        this.users.load(this.getClass().getClassLoader().getResourceAsStream("users.properties"));
    }

    @Override
    public boolean userExists(String username) {
        if (username == null) {
            return Boolean.FALSE;
        }

        if (this.users.get(username) != null) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public User getUser(String username) {
        String pwd = (String) this.users.get(username);
        return new PermissionedUser(username, pwd);
    }

    @Override
    public Permissions getPermissions(String username) {
        String permissions =  ((PermissionedUser) this.getUser(username)).getPermissions();
        return () -> { return permissions; };
    }
    
}
