package tennisclub.facade;

import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.dto.user.UserUpdateDTO;
import tennisclub.enums.Role;

import java.util.List;

/**
 * Facade for manipulation with users
 * @author Ondrej Holub
 */
public interface UserFacade {

    /**
     * Registers a new user
     * @param userAuthDTO containing the user authentication data
     * @return full user dto
     */
    UserFullDTO register(UserAuthDTO userAuthDTO);

    /**
     * Authenticates user, appropriate
     * @param userAuthDTO containing the user authentication data
     * @return JWT
     */
    String authenticate(UserAuthDTO userAuthDTO);

    /**
     * Finds all users in the system
     * @return List of Users
     */
    List<UserFullDTO> findAllUsers();

    /**
     * Finds a user based on id
     * @param userId of the user
     * @return the user
     */
    UserFullDTO findUserById(Long userId);

    /**
     * Finds a user based on name
     * @param name of the user
     * @return the user
     */
    List<UserFullDTO> findUsersByName(String name);

    /**
     * Finds a user based on username
     * @param username of the user
     * @return the user
     */
    UserFullDTO findUserByUsername(String username);

    /**
     * Finds a user based on email
     * @param email of the user
     * @return the user
     */
    List<UserFullDTO> findUsersByEmail(String email);

    /**
     * Updated the data of a user
     * @param userUpdateDTO containing the new user data
     * @return the updated user
     */
    UserFullDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);

    /**
     * Removes a user from the system
     * @param id of the user to be removed
     */
    void removeUser(Long id);
}
