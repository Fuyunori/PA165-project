package tennisclub.dto.user;

import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * User DTO containing authentication data
 * @author Ondrej Holub
 */
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
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
