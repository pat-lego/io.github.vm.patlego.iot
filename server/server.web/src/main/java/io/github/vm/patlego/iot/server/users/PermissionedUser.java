package io.github.vm.patlego.iot.server.users;

public class PermissionedUser implements User, Permissions {

    protected String username;
    protected String password; 

    public PermissionedUser(String username, String password) {
        this.password = password;

        this.username = username;
    }

    @Override
    public String getPermissions() {
        return this.password.split(":")[1];
    }

    @Override
    public String getPassword() {
        return this.password.split(":")[0];
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
