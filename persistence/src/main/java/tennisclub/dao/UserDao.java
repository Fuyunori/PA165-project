package tennisclub.dao;

import tennisclub.entity.User;

import java.util.List;

/**
 * DAO interface for CRUD operations on User
 * @author Ondrej Holub
 */
public interface UserDao {

    /**
     * Persists a new User
     * @param user which is to be persisted
     */
    void create(User user);

    /**
     * Finds a unique User by its primary key
     * @param id part and
     *
     * @return matching user
     */
    User findById(Long id);

    /**
     * Finds Users with a name containing provided name
     * @param name of the user
     * @return list of Users
     */
    List<User> findByName(String name);

    /**
     * Finds Users with a given username (which is unique)
     * @param username of the user
     * @return a single User
     */
    User findByUsername(String username);

    /**
     * Finds Users with a given email
     * @param email of the user
     * @return a list of Users with the provided email
     */
    List<User> findByEmail(String email);

    /**
     * Finds all Users in the system
     * @return a list of Users
     */
    List<User> findAll();

    /**
     * Updates a User
     * @param user to be updated
     */
    User update(User user);

    /**
     * Deletes a User
     * @param user to be deleted
     */
    void delete(User user);
}
