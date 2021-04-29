package tennisclub.dto;

import javax.validation.constraints.NotNull;

public class UserAuthDTO {

    @NotNull
    private  String username;

    @NotNull
    private  String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPasswordHash(String password) {
        this.password = password;
    }
}
