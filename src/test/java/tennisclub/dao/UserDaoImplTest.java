package tennisclub.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.User;
import tennisclub.entity.enums.Role;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoImplTest {
    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    void notNullName() {
        assertThrows(DataIntegrityViolationException.class, () -> userDao.create(new User()));
    }

    @Test
    @Transactional
    void findByUsername() {
        User createdUser = createUser("honza42", "hon@za.cz", "Honza", Role.USER);

        List<User> foundUsers = userDao.findByUsername("honza42");
        assertThat(foundUsers.size()).isEqualTo(1);
        assertUsersMatch(foundUsers.get(0), createdUser);
    }

    private User createUser(String username, String email, String name, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);
        user.setPasswordHash("abcdef123456");
        userDao.create(user);
        return user;
    }

    private void assertUsersMatch(User user1, User user2) {
        assertThat(user1).isNotNull();
        assertThat(user2).isNotNull();
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getUsername()).isEqualTo(user2.getUsername());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user1.getRole()).isEqualTo(user2.getRole());
    }
}