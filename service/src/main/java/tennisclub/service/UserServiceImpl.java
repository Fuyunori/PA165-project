package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tennisclub.dao.UserDao;
import tennisclub.entity.User;
import tennisclub.entity.enums.Role;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private  final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public void register(User user, String plainTextPassword) {
        user.setPasswordHash(passwordEncoder.encode(plainTextPassword));
        userDao.create(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public boolean authenticate(User user, String plainTextPassword) {
        return passwordEncoder.matches(plainTextPassword, user.getPasswordHash());
    }

    @Override
    public boolean hasRights(User user, Role role) {
        if(role == Role.MANAGER) {
            return user.getRole() == Role.MANAGER;
        }
        return role != Role.USER || user.getRole() != null;
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
    public void updateUserData(User user) {
        userDao.update(user);
    }

    @Override
    public void removeUser(User user) {
        userDao.delete(user);
    }
}
