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

    private String username;
    private String password;
    private UserAuthDTO authDto;
    private User authEntity;

    @BeforeEach
    void init() {
        username = "us3rn4m3";
        password = "p4ssw0rd";

        authDto = new UserAuthDTO();
        authDto.setUsername(username);
        authDto.setPassword(password);

        authEntity = new User();
        authEntity.setUsername(authDto.getUsername());
    }

    @Test
    void register() {
        ArgumentCaptor<User> passedEntity = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> passedPassword = ArgumentCaptor.forClass(String.class);

        userFacade.register(authDto);
        verify(userService).register(passedEntity.capture(), passedPassword.capture());
        assertThat(passedEntity.getValue().getUsername()).isEqualTo(username);
        assertThat(passedPassword.getValue()).isEqualTo(password);
    }

    @Test
    void authenticate() {
        when(userService.authenticate(authEntity, password)).thenReturn(true);
        ArgumentCaptor<User> passedEntity = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> passedPassword = ArgumentCaptor.forClass(String.class);

        assertThat(userFacade.authenticate(authDto)).isTrue();
        verify(userService).authenticate(passedEntity.capture(), passedPassword.capture());
        assertThat(passedEntity.getValue().getUsername()).isEqualTo(username);
        assertThat(passedPassword.getValue()).isEqualTo(password);
    }

    @Test
    void forwardAuthFailure() {
        when(userService.authenticate(authEntity, password)).thenReturn(false);
        assertThat(userFacade.authenticate(authDto)).isFalse();
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
