package tennisclub.facade;

import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
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
     * Authenticates user
     * @param userAuthDTO containing the user authentication data
     */
    boolean authenticate(UserAuthDTO userAuthDTO);

    /**
     * Verifies whether the user has certain rights
     * @param userDTO containing the user data
     * @param role the user should have
     * @return whether the user was authorised
     */
    boolean hasRights(UserDTO userDTO, Role role);

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
     * @param userFullDTO containing the new user data
     * @return the updated user
     */
    UserFullDTO updateUser(UserFullDTO userFullDTO);

    /**
     * Removes a user from the system
     * @param id of the user to be removed
     */
    void removeUser(Long id);
}
