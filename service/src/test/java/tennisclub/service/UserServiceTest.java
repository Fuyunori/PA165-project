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
        user.setId(1L);
        user.setUsername("destroyer777");
        user.setRole(Role.USER);
        user.setName("Karel");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("notJohn");
        otherUser.setRole(Role.USER);
        otherUser.setName("Honza");

        manager = new User();
        manager.setId(3L);
        manager.setUsername("tennis_hustler");
        manager.setRole(Role.MANAGER);
        manager.setName("Karel");

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

    @Test
    void findByUserId() {
        when(userDao.findById(1L)).thenReturn(user);
        when(userDao.findById(2L)).thenReturn(otherUser);
        when(userDao.findById(3L)).thenReturn(manager);
        when(userDao.findById(42L)).thenReturn(null);

        User foundUser;

        foundUser = userService.findUserById(1L);
        verify(userDao).findById(1L);
        assertThat(foundUser).isEqualTo(user);

        foundUser = userService.findUserById(2L);
        verify(userDao).findById(2L);
        assertThat(foundUser).isEqualTo(otherUser);

        foundUser = userService.findUserById(3L);
        verify(userDao).findById(3L);
        assertThat(foundUser).isEqualTo(manager);

        foundUser = userService.findUserById(42L);
        verify(userDao).findById(42L);
        assertThat(foundUser).isNull();
    }

    @Test
    void findUsersByName() {
        when(userDao.findByName("Karel")).thenReturn(List.of(user, manager));
        List<User> users = userService.findUsersByName("Karel");

        verify(userDao).findByName("Karel");
        assertThat(users).hasSize(2);
        assertThat(users).contains(user, manager);
    }
}
