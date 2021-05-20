package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tennisclub.dao.UserDao;
import tennisclub.entity.User;
import tennisclub.enums.Role;
import tennisclub.exceptions.ForbiddenException;
import tennisclub.exceptions.UnauthorisedException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private User userWithHash;
    private User otherUser;
    private User manager;
    private User managerWithHash;
    private String password;
    private String otherPassword;

    @BeforeEach
    void initUsers() {
        user = new User();
        user.setId(1L);
        user.setUsername("destroyer777");
        user.setRole(Role.USER);
        user.setName("Karel");
        user.setEmail("destroyer@localhost");


        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("notJohn");
        otherUser.setRole(Role.USER);
        otherUser.setName("Honza");
        otherUser.setEmail("honza@localhost");

        manager = new User();
        manager.setId(3L);
        manager.setUsername("tennis_hustler");
        manager.setRole(Role.MANAGER);
        manager.setName("Karel");
        manager.setEmail("karel@localhost");

        password = "Never.gonna_give-You up!";
        otherPassword = "my L17713 pony";

        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        userWithHash = new User();
        userWithHash.setUsername(user.getUsername());
        userWithHash.setPasswordHash(passwordEncoder.encode(password));
        userWithHash.setRole(Role.USER);

        managerWithHash = new User();
        managerWithHash.setUsername(manager.getUsername());
        managerWithHash.setPasswordHash(passwordEncoder.encode(otherPassword));
        managerWithHash.setRole(Role.MANAGER);
    }

    @Test
    void register() {
        User newUser = userService.register(user, password);
        verify(userDao).create(user);
        assertThat(user.getPasswordHash()).isNotBlank();
        assertThat(newUser.getRole()).isEqualTo(Role.USER);
        assertThat(newUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(newUser.getPasswordHash()).isEqualTo(user.getPasswordHash());
    }

    @Test
    void authenticate() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(userWithHash);
        userService.register(user, password);
        assertThat(userService.authenticateJWT(user.getUsername(), password)).isNotNull();
    }

    @Test
    void authenticateWithWrongPassword() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(userWithHash);
        userService.register(user, password);
        assertThatThrownBy(() ->
            userService.authenticateJWT(user.getUsername(), otherPassword)
        ).isInstanceOf(UnauthorisedException.class);
    }

    @Test
    void verifyRoleUser() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(userWithHash);
        String userJWT = userService.authenticateJWT(user.getUsername(), password);
        assertThatNoException().isThrownBy( () -> userService.verifyRole(userJWT, Role.USER));
        assertThatThrownBy( () ->
            userService.verifyRole(userJWT, Role.MANAGER)
        ).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void verifyRoleManager() {
        when(userDao.findByUsername(manager.getUsername())).thenReturn(managerWithHash);
        String managerJWT = userService.authenticateJWT(manager.getUsername(), otherPassword);
        assertThatNoException().isThrownBy( () -> userService.verifyRole(managerJWT, Role.USER));
        assertThatNoException().isThrownBy( () -> userService.verifyRole(managerJWT, Role.MANAGER));
    }

    @Test
    void verifyUser() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(userWithHash);
        String userJWT = userService.authenticateJWT(user.getUsername(), password);
        assertThatNoException().isThrownBy( () -> userService.verifyUser(userJWT, user.getUsername()));
    }

    @Test
    void verifyInvalidUser() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(userWithHash);
        String userJWT = userService.authenticateJWT(user.getUsername(), password);

        assertThatThrownBy( () ->
            userService.verifyUser(userJWT, otherUser.getUsername())
        ).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void verifyUserOrManager() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(userWithHash);
        when(userDao.findByUsername(manager.getUsername())).thenReturn(managerWithHash);

        String managerJWT = userService.authenticateJWT(manager.getUsername(), otherPassword);
        String userJWT = userService.authenticateJWT(user.getUsername(), password);

        assertThatNoException().isThrownBy( () -> userService.verifyUserOrManager(managerJWT, user.getUsername()));
        assertThatNoException().isThrownBy( () -> userService.verifyUserOrManager(userJWT, user.getUsername()));
        assertThatThrownBy( () ->
                userService.verifyUserOrManager(userJWT, otherUser.getUsername())
        ).isInstanceOf(ForbiddenException.class);
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

    @Test
    void findUserByUsername() {
        String username = user.getUsername();
        String nonexUsername = "someUsernameThatDoesNotExist";
        when(userDao.findByUsername(username)).thenReturn(user);
        when(userDao.findByUsername(nonexUsername)).thenReturn(null);

        assertThat(userService.findUserByUsername(username)).isEqualTo(user);
        verify(userDao).findByUsername(username);

        assertThat(userService.findUserByUsername(nonexUsername)).isNull();
        verify(userDao).findByUsername(nonexUsername);
    }

    @Test
    void findUsersByEmail() {
        String email = user.getEmail();
        String nonexEmail = "some.email.that.does.not@exi.st";
        when(userDao.findByEmail(email)).thenReturn(List.of(user));
        when(userDao.findByEmail(nonexEmail)).thenReturn(List.of());

        assertThat(userService.findUsersByEmail(email)).hasSize(1).contains(user);
        verify(userDao).findByEmail(email);

        assertThat(userService.findUsersByEmail(nonexEmail)).isEmpty();
        verify(userDao).findByEmail(nonexEmail);
    }

    @Test
    void updateUserData() {
        when(userDao.findById(user.getId())).thenReturn(user);
        when(userDao.update(user)).thenReturn(otherUser);
        assertThat(userService.updateUserData(user)).isEqualTo(otherUser); // return value propagates correctly
        verify(userDao).update(user);
    }

    @Test
    void removeUser() {
        userService.removeUser(user);
        verify(userDao).delete(user);
    }
}
