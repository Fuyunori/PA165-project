package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dao.UserDao;
import tennisclub.entity.User;
import tennisclub.enums.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pavel Tobiáš
 */
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserDao userDao;

    @Autowired
    private UserService userService;

    private User user;
    private User otherUser;
    private User manager;
    private String password;
    private String otherPassword;

    @BeforeEach
    void initUsers() {
        user = new User();
        user.setRole(Role.USER);

        otherUser = new User();
        otherUser.setRole(Role.USER);

        manager = new User();
        manager.setRole(Role.MANAGER);

        password = "Never.gonna_give-You up!";
        otherPassword = "my L17713 pony";
    }

    @Test
    void register() {
        userService.register(user, password);
        verify(userDao).create(user);
        assertThat(user.getPasswordHash()).isNotBlank();
    }

    @Test
    void authenticate() {
        userService.register(user, password);
        assertThat(userService.authenticate(user, password)).isTrue();
    }

    @Test
    void authenticateWithWrongPassword() {
        userService.register(user, password);
        assertThat(userService.authenticate(user, otherPassword)).isFalse();
    }

    @Test
    void hasRights() {
        assertThat(userService.hasRights(user, Role.USER)).isTrue();
        assertThat(userService.hasRights(user, Role.MANAGER)).isFalse();
        assertThat(userService.hasRights(manager, Role.USER)).isTrue();
        assertThat(userService.hasRights(manager, Role.MANAGER)).isTrue();
    }

    @Test
    void getAllUsers() {
        when(userDao.findAll()).thenReturn(List.of(user, manager, otherUser));
        List<User> users = userService.getAllUsers();

        verify(userDao).findAll();
        assertThat(users).hasSize(3);
        assertThat(users).contains(user, otherUser, manager);
    }
}
