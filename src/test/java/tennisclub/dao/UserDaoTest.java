package tennisclub.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import tennisclub.entity.User;
import tennisclub.entity.enums.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for User.
 * @author Pavel Tobiáš
 */
@SpringBootTest
class UserDaoTest {
    @PersistenceContext
    EntityManager manager;

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    void notNullValidation() {
        User blankUser = new User();
        assertThrows(DataIntegrityViolationException.class, () -> userDao.create(blankUser));

        User userWithUsername = new User();
        userWithUsername.setUsername("username666");
        assertDoesNotThrow(() -> userDao.create(userWithUsername));
    }

    @Test
    @Transactional
    void checkAllFieldsPersisted() {
        User createdUser = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        userDao.create(createdUser);

        User foundUser = manager.createQuery("select u from User u", User.class).getSingleResult();
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
        manager.persist(createdUser1);
        manager.persist(createdUser2);
        manager.persist(createdUser3);

        List<User> foundUsers = userDao.findAll();
        assertThat(foundUsers.size()).isEqualTo(3);
        assertThat(foundUsers).contains(createdUser1);
        assertThat(foundUsers).contains(createdUser2);
        assertThat(foundUsers).contains(createdUser3);
    }

    @Test
    @Transactional
    void findById() {
        User createdUser1 = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        User createdUser2 = createUser("pepa42", "pe@pa.cz", "Pepa", Role.MANAGER);
        assertThat(createdUser1).isNotEqualTo(createdUser2);

        manager.persist(createdUser1);
        manager.persist(createdUser2);

        User foundUser1 = userDao.findById(createdUser1.getId());
        User foundUser2 = userDao.findById(createdUser2.getId());
        assertThat(foundUser1).isNotEqualTo(foundUser2);
        assertThat(foundUser1).isEqualTo(createdUser1);
        assertThat(foundUser2).isEqualTo(createdUser2);
    }

    @Test
    @Transactional
    void findByUsername() {
        User createdUser1 = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        User createdUser2 = createUser("pepa42", "pe@pa.cz", "Pepa", Role.USER);
        User createdUser3 = createUser("dalsi_honza42", "dalsi.hon@za.cz", "Honza", Role.USER);
        manager.persist(createdUser1);
        manager.persist(createdUser2);
        manager.persist(createdUser3);

        User foundUser = userDao.findByUsername("honza42");
        assertThat(foundUser).isEqualTo(createdUser1);
        assertThat(foundUser).isNotEqualTo(createdUser2);
        assertThat(foundUser).isNotEqualTo(createdUser3);
    }

    @Test
    @Transactional
    void findByNameSubstring() {
        User createdUser1 = createUser("honza42", "hon@za.cz", "Honza Koleno", Role.USER);
        User createdUser2 = createUser("lolek34", "lol@ek.cz", "Lolek", Role.MANAGER);
        User createdUser3 = createUser("kratos", "kra@tos.cz", "Kratos", Role.USER);
        manager.persist(createdUser1);
        manager.persist(createdUser2);
        manager.persist(createdUser3);

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
        manager.persist(createdUser1);
        manager.persist(createdUser2);
        manager.persist(createdUser3);

        List<User> foundUsers = userDao.findByEmail("hon@za.cz");
        assertThat(foundUsers.size()).isEqualTo(1);
        assertThat(foundUsers).contains(createdUser1);
        assertThat(foundUsers).doesNotContain(createdUser2);
        assertThat(foundUsers).doesNotContain(createdUser3);
    }

    @Test
    @Transactional
    void updateAfterDetached() {
        manager.persist(createUser("user42", "user42@domain.cz", "Alice", Role.USER));

        User alice = manager.createQuery("select u from User u", User.class).getSingleResult();
        assertThat(alice.getName()).isEqualTo("Alice");

        alice.setName("Bob");
        User bob = manager.createQuery("select u from User u", User.class).getSingleResult();
        assertThat(bob.getName()).isEqualTo("Bob");

        manager.detach(bob);
        bob.setName("Charlie");
        User stillBob = manager.createQuery("select u from User u", User.class).getSingleResult();
        assertThat(stillBob.getName()).isEqualTo("Bob");

        userDao.update(bob);
        User charlie = manager.createQuery("select u from User u", User.class).getSingleResult();
        assertThat(charlie.getName()).isEqualTo("Charlie");
    }

    @Test
    @Transactional
    public void delete() {
        User user = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        manager.persist(user);
        userDao.delete(user);
        assertThat(manager.find(User.class, user.getId())).isNull();
    }

    @Test
    @Transactional
    public void deleteAfterDetach() {
        User user = createUser("honza42", "hon@za.cz", "Honza", Role.USER);
        manager.persist(user);
        manager.detach(user);
        userDao.delete(user);
        assertThat(manager.find(User.class, user.getId())).isNull();
    }

    private User createUser(String username, String email, String name, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);
        user.setPasswordHash("abcdef123456");
        return user;
    }
}