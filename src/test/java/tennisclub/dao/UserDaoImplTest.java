package tennisclub.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.User;
import tennisclub.entity.enums.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoImplTest {
    @PersistenceContext
    EntityManager manager;

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    void notNullName() {
        assertThrows(DataIntegrityViolationException.class, () -> userDao.create(new User()));
    }

    @Test
    @Transactional
    void checkAllFieldsPersisted() {
        User createdUser = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        User foundUser = userDao.findAll().get(0);

        assertThat(foundUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo(foundUser.getName());
        assertThat(createdUser.getUsername()).isEqualTo(foundUser.getUsername());
        assertThat(createdUser.getEmail()).isEqualTo(foundUser.getEmail());
        assertThat(createdUser.getRole()).isEqualTo(foundUser.getRole());
    }

    @Test
    @Transactional
    void findAll() {
        User createdUser1 = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        User createdUser2 = createUser("pepa42", "pe@pa.cz", "Pepa", Role.MANAGER);
        User createdUser3 = createUser("dalsi_honza42", "dalsi.hon@za.cz", "Honza", Role.USER);

        List<User> foundUsers = userDao.findAll();
        assertThat(foundUsers.size()).isEqualTo(3);
        assertThat(foundUsers).contains(createdUser1);
        assertThat(foundUsers).contains(createdUser2);
        assertThat(foundUsers).contains(createdUser3);
    }

    @Test
    @Transactional
    void findByUsername() {
        User createdUser1 = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        User createdUser2 = createUser("pepa42", "pe@pa.cz", "Pepa", Role.USER);
        User createdUser3 = createUser("dalsi_honza42", "dalsi.hon@za.cz", "Honza", Role.USER);

        List<User> foundUsers = userDao.findByUsername("honza42");
        assertThat(foundUsers.size()).isEqualTo(1);
        assertThat(foundUsers).contains(createdUser1);
        assertThat(foundUsers).doesNotContain(createdUser2);
        assertThat(foundUsers).doesNotContain(createdUser3);
    }

    @Test
    @Transactional
    void findByNameSubstring() {
        User createdUser1 = createUser("honza42", "hon@za.cz", "Honza Koleno", Role.USER);
        User createdUser2 = createUser("lolek34", "lol@ek.cz", "Lolek", Role.MANAGER);
        User createdUser3 = createUser("kratos", "kra@tos.cz", "Kratos", Role.USER);

        List<User> foundUsers = userDao.findByName("ole");
        assertThat(foundUsers.size()).isEqualTo(2);
        assertThat(foundUsers).contains(createdUser1);
        assertThat(foundUsers).contains(createdUser2);
        assertThat(foundUsers).doesNotContain(createdUser3);
    }

    @Test
    @Transactional
    void findByEmail() {
        User createdUser1 = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        User createdUser2 = createUser("druhy_honza42", "druhy.hon@za.cz", "Honza", Role.USER);
        User createdUser3 = createUser("pepa42", "pe@pa.cz", "Pepa", Role.USER);

        List<User> foundUsers = userDao.findByEmail("hon@za.cz");
        assertThat(foundUsers.size()).isEqualTo(1);
        assertThat(foundUsers).contains(createdUser1);
        assertThat(foundUsers).doesNotContain(createdUser2);
        assertThat(foundUsers).doesNotContain(createdUser3);
    }

    @Test
    @Transactional
    void updateAfterDetached() {
        createUser("theSameUser", "user42@domain.cz", "Alice", Role.USER);
        User alice = userDao.findByUsername("theSameUser").get(0);
        assertThat(alice.getName()).isEqualTo("Alice");

        alice.setName("Bob");
        User bob =  userDao.findByUsername("theSameUser").get(0);
        assertThat(bob.getName()).isEqualTo("Bob");

        manager.detach(bob);
        bob.setName("Charlie");
        User stillBob = userDao.findByUsername("theSameUser").get(0);
        assertThat(stillBob.getName()).isEqualTo("Bob");

        userDao.update(bob);
        User charlie = userDao.findByUsername("theSameUser").get(0);
        assertThat(charlie.getName()).isEqualTo("Charlie");
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
}