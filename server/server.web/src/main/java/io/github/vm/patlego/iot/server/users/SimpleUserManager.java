package io.github.vm.patlego.iot.server.users;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);

        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        this.users.entrySet().forEach(entry -> {
            User user = new User();
            user.setUsername((String) entry.getKey());
            user.setPassword((String) entry.getValue());

            userList.add(user);
        });

        return userList;
    }
    
}
