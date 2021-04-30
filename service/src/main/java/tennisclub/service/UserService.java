package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.User;
import tennisclub.enums.Role;

import java.util.List;

/**
 * Service for manipulation with users
 * @author Ondrej Holub
 */
@Service
public interface UserService {

    /**
     * Registers a new User
     * @param user to be registered
     * @param plainTextPassword of the user
     */
    void register(User user, String plainTextPassword);

    /**
     * Finds all users in the system
     * @return List of Users
     */
    List<User> getAllUsers();

    /**
     * Authenticates a user with his/her password
     * @param user to be authenticated
     * @param plainTextPassword of the user
     * @return whether user was authenticated
     */
    boolean authenticate(User user, String plainTextPassword);

    /**
     * Verifies whether the user has certain rights
     * @param user to be authorised
     * @param role the user should have
     * @return whether the user was authorised
     */
    boolean hasRights(User user, Role role);

    /**
     * Finds a user based on id
     * @param id of the user
     * @return the user
     */
    User findUserById(Long id);

    /**
     * Finds a user based on name
     * @param name of the user
     * @return the user
     */
    List<User> findUsersByName(String name);

    /**
     * Finds a user based on username
     * @param username of the user
     * @return the user
     */
    User findUserByUsername(String username);

    /**
     * Finds a user based on email
     * @param email of the user
     * @return the user
     */
    List<User> findUsersByEmail(String email);

    /**
     * Updated the data of a user
     * @param user to be updated
     * @return the updated user
     */
    User updateUserData(User user);

    /**
     * Removes a user from the system
     * @param user to be removed
     */
    void removeUser(User user);
}
