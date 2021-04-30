package tennisclub.facade;

import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.enums.Role;

import java.util.List;

public interface UserFacade {

    void register(UserAuthDTO userAuthDTO);

    boolean authenticate(UserAuthDTO userAuthDTO);

    boolean hasRights(UserDTO userDTO, Role role);

    List<UserFullDTO> findAllUsers();

    UserFullDTO findUserById(Long userId);

    List<UserFullDTO> findUsersByName(String name);

    UserFullDTO findUserByUsername(String username);

    List<UserFullDTO> findUsersByEmail(String email);

    UserFullDTO updateUser(UserFullDTO userFullDTO);

    void removeUser(UserDTO userDTO);
}
