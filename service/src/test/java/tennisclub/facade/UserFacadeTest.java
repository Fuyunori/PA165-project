package tennisclub.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.entity.User;
import tennisclub.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserFacadeTest {
    @MockBean
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    private UserAuthDTO authDto;
    private User authEntity;

    @BeforeEach
    void init() {
        authDto = new UserAuthDTO();
        authDto.setUsername("us3rn4m3");
        authDto.setPassword("p4ssw0rd");

        authEntity = new User();
        authEntity.setUsername(authDto.getUsername());
    }

    @Test
    void register() {
        String username = authDto.getUsername();
        String password = authDto.getPassword();

        ArgumentCaptor<User> passedEntity = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> passedPassword = ArgumentCaptor.forClass(String.class);

        userFacade.register(authDto);
        verify(userService).register(passedEntity.capture(), passedPassword.capture());
        assertThat(passedEntity.getValue().getUsername()).isEqualTo(username);
        assertThat(passedPassword.getValue()).isEqualTo(password);
    }

    @Test
    void authenticate() {
        // TODO
    }

    @Test
    void hasRights() {
        // TODO
    }

    @Test
    void findAllUsers() {
        // TODO
    }

    @Test
    void findUserById() {
        // TODO
    }

    @Test
    void findUsersByName() {
        // TODO
    }

    @Test
    void findUserByUsername() {
        // TODO
    }

    @Test
    void findUsersByEmail() {
        // TODO
    }

    @Test
    void updateUser() {
        // TODO
    }

    @Test
    void removeUser() {
        // TODO
    }
}
