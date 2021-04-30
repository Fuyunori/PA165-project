package tennisclub.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.service.UserService;

@SpringBootTest
public class UserFacadeTest {
    @MockBean
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    @Test
    void register() {
        // TODO
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
