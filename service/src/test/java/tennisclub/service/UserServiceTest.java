package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dao.UserDao;
import tennisclub.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

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
    private String password;

    @BeforeEach
    void initUsers() {
        user = new User();
        password = "Never.gonna_give-You up!";
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
}
