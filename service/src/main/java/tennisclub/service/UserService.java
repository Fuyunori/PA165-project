package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.User;
import tennisclub.enums.Role;
import tennisclub.exceptions.ForbiddenException;
import tennisclub.exceptions.UnauthorisedException;

import java.util.List;

/**
 * Service for manipulation with users
 * @author Ondrej Holub
 */
@Service
public interface UserService {

    /**
     * Registers a new user
     * @param user to be registered
     * @param plainTextPassword of the user
     * @return newly created user
     */
    User register(User user, String plainTextPassword);

    /**
     * Finds all users in the system
     * @return List of Users
     */
    List<User> getAllUsers();

    /**
     * Authenticates a user with his/her password
     * @param username to be authenticated
     * @param plainTextPassword of the user
     * @return JWT
     * @throws UnauthorisedException if the combination of username/password is invalid
     */
    String authenticateJWT(String username, String plainTextPassword);

    /**
     * Verifies whether the user has certain role
     * @param jwt provided by the user
     * @param expectedRole the user should have
     * @throws UnauthorisedException if the jwt is invalid
     * @throws ForbiddenException if the role of the user is not sufficient
     */
    void verifyRole(String jwt, Role expectedRole);

    /**
     * Verifies whether the user is who they claim to be
     * @param jwt provided by the user
     * @param expectedUsername who the user should be
     * @throws UnauthorisedException if the jwt is invalid
     * @throws ForbiddenException if the user is not the expected user
     */
    void verifyUser(String jwt, String expectedUsername);

    /**
     * Verifies whether the user is who they claim to be
     * @param jwt provided by the user
     * @param expectedUserId who the user should be
     * @throws UnauthorisedException if the jwt is invalid
     * @throws ForbiddenException if the user is not the expected user
     */
    void verifyUser(String jwt, Long expectedUserId);

    /**
     * Verifies whether the user is who they claim to be or a manager
     * @param jwt provided by the user
     * @param expectedUsername who the user should be
     * @throws UnauthorisedException if the jwt is invalid
     * @throws ForbiddenException if the user is not the expected user
     */
    void verifyUserOrManager(String jwt, String expectedUsername);

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
