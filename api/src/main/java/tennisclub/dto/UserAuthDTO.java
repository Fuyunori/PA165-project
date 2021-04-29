package tennisclub.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserAuthDTO {

    @NotBlank
    private  String username;

    @NotBlank
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAuthDTO)) return false;

        UserAuthDTO that = (UserAuthDTO) o;

        return getUsername() != null ? getUsername().equals(that.getUsername()) : that.getUsername() == null;
    }

    @Override
    public int hashCode() {
        return getUsername() != null ? getUsername().hashCode() : 0;
    }
}
