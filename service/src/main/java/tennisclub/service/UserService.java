package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.User;
import tennisclub.entity.enums.Role;

import java.util.List;

@Service
public interface UserService {

    void register(User user, String plainTextPassword);

    List<User> getAllUsers();

    boolean login(User user, String plainTextPassword);

    boolean hasRights(User user, Role role);

    User findUserById(Long id);

    User findUserByEmail(String email);
}
