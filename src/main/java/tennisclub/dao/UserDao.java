package tennisclub.dao;

import tennisclub.entity.User;

import java.util.List;

/**
 * @author Ondrej Holub
 */
public interface UserDao {
    void create(User user);

    User findById(Long id);

    List<User> findByName(String name);

    List<User> findByUsername(String username);

    List<User> findByEmail(String email);

    List<User> findAll();

    void update(User user);

    void delete(User user);
}
