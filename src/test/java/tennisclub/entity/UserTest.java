package tennisclub.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserTest {
    @Test
    void equalityBasics() {
        User user = new User();
        User otherUser = new User();

        assertThat(user).isEqualTo(user);
        assertThat(user).isNotEqualTo(null);
        assertThat(user).isNotEqualTo(new Object());
        assertThat(user).isNotEqualTo(otherUser);
    }

    @Test
    void equalityOnSameUsername() {
        User user = new User();
        User otherUser = new User();

        user.setUsername("user42");
        otherUser.setUsername("user42");
        user.setEmail("some@email.com");
        otherUser.setEmail("different@email.org");
        user.setName("One Name");
        otherUser.setName("Another Name");

        assertThat(user).isEqualTo(otherUser);
    }

    @Test
    void nonequalityOnDifferentUsername() {
        User user = new User();
        User otherUser = new User();

        user.setUsername("one_username");
        otherUser.setUsername("different_username");
        user.setEmail("same@email.com");
        otherUser.setEmail("same@email.com");
        user.setName("Same Name");
        otherUser.setName("Same Name");

        assertThat(user).isNotEqualTo(otherUser);
    }
}
