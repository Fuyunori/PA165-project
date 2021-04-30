package tennisclub.facade;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.entity.User;
import tennisclub.enums.Role;
import tennisclub.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserFacadeImpl implements UserFacade {

    private final Mapper mapper;
    private final UserService userService;

    @Autowired
    public UserFacadeImpl(Mapper mapper, UserService userService) {
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    public void register(UserAuthDTO userAuthDTO) {
        User user = mapper.map(userAuthDTO, User.class);
        userService.register(user, userAuthDTO.getPassword());
    }

    @Override
    public boolean authenticate(UserAuthDTO userAuthDTO) {
        User user = mapper.map(userAuthDTO, User.class);
        return userService.authenticate(user, userAuthDTO.getPassword());
    }

    @Override
    public boolean hasRights(UserDTO userDTO, Role role) {
        User user = mapper.map(userDTO, User.class);
        return userService.hasRights(user, role);
    }

    @Override
    public List<UserFullDTO> findAllUsers() {
        List<User> foundUsers = userService.getAllUsers();
        return foundUsers.stream()
                .map(u -> mapper.map(u, UserFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserFullDTO findUserById(Long userId) {
        User foundUser = userService.findUserById(userId);
        return mapper.map(foundUser, UserFullDTO.class);
    }

    @Override
    public List<UserFullDTO> findUsersByName(String name) {
        List<User> foundUsers = userService.findUsersByName(name);
        return foundUsers.stream()
                .map(u -> mapper.map(u, UserFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserFullDTO findUserByUsername(String username) {
        User foundUser = userService.findUserByUsername(username);
        return mapper.map(foundUser, UserFullDTO.class);
    }

    @Override
    public List<UserFullDTO> findUsersByEmail(String email) {
        List<User> foundUsers = userService.findUsersByEmail(email);
        return foundUsers.stream()
                .map(u -> mapper.map(u, UserFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserFullDTO updateUser(UserFullDTO userFullDTO) {
        User user = mapper.map(userFullDTO, User.class);
        user = userService.updateUserData(user);
        return mapper.map(user, UserFullDTO.class);
    }

    @Override
    public void removeUser(UserDTO userDTO) {
        User user = mapper.map(userDTO, User.class);
        userService.removeUser(user);
    }
}
