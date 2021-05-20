package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tennisclub.dao.UserDao;
import tennisclub.entity.User;
import tennisclub.enums.Role;
import tennisclub.exceptions.ForbiddenException;
import tennisclub.exceptions.UnauthorisedException;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Implementation of {@link UserService}
 *
 * @author Ondrej Holub
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public User register(User user, String plainTextPassword) {
        user.setPasswordHash(passwordEncoder.encode(plainTextPassword));
        user.setRole(Role.USER);
        userDao.create(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public String authenticateJWT(String username, String plainTextPassword) {
        User user = findUserOrThrowUnauthorised(username);

        if (passwordEncoder.matches(plainTextPassword, user.getPasswordHash())) {
            return JWTService.createJWT(user);
        }

        throw new UnauthorisedException();
    }

    @Override
    public void verifyRole(String jwt, Role expectedRole) {
        Role role = getRoleOrThrowUnauthorised(jwt);

        switch (expectedRole) {
            case MANAGER:
                if (role != Role.MANAGER) {
                    throw new ForbiddenException();
                }
                break;

            case USER:
            default:
                if (role == null) {
                    throw new ForbiddenException();
                }
                break;
        }
    }

    @Override
    public void verifyUser(String jwt, String expectedUsername) {
        String username = getUsernameOrThrowUnauthorised(jwt);
        if (!username.equals(expectedUsername)) {
            throw new ForbiddenException();
        }
    }

    public void verifyUserOrManager(String jwt, String expectedUsername) {
        String username = getUsernameOrThrowUnauthorised(jwt);
        Role role = getRoleOrThrowUnauthorised(jwt);

        if ( (!username.equals(expectedUsername)) && role != Role.MANAGER) {
            throw new ForbiddenException();
        }
    }

    @Override
    public User findUserById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findUsersByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> findUsersByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User updateUserData(User user) {
        User originalUser = findUserById(user.getId());
        originalUser.setName(user.getName());
        originalUser.setEmail(user.getEmail());
        return userDao.update(originalUser);
    }

    @Override
    public void removeUser(User user) {
        userDao.delete(user);
    }

    private User findUserOrThrowUnauthorised(String username) {
        User user;
        try {
            user = userDao.findByUsername(username);
        } catch (NoResultException e) {
            throw new UnauthorisedException();
        }
        return user;
    }

    private String getUsernameOrThrowUnauthorised(String jwt) {
        String username;
        try {
            username = JWTService.decodeJWT(jwt).getSubject();
        } catch (Exception e) {
            throw new UnauthorisedException();
        }
        return username;
    }

    private Role getRoleOrThrowUnauthorised(String jwt) {
        String stringRole;
        try {
            stringRole = JWTService.decodeJWT(jwt).get("role").toString();
        } catch (Exception e) {
            throw new UnauthorisedException();
        }
        Role role;
        switch (stringRole) {
            case "USER":
                role = Role.USER;
                break;
            case "MANAGER":
                role = Role.MANAGER;
                break;
            default:
                role = null;
        }
        return role;
    }
}
