package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.entity.User;
import tennisclub.facade.UserFacade;

import java.util.List;

/**
 * Controller for manipulation with users
 * @author Ondrej Holub
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping
    public final UserFullDTO register(@RequestBody UserAuthDTO userAuthDTO) {
        return userFacade.register(userAuthDTO);
    }

    @GetMapping("/auth")
    public final boolean authenticate(@RequestBody UserAuthDTO userAuthDTO) {
        return userFacade.authenticate(userAuthDTO);
    }

    @GetMapping
    public final List<UserFullDTO> findAllUsers() {
        return userFacade.findAllUsers();
    }

    @GetMapping("/{id}")
    public final UserFullDTO findUserById(@PathVariable Long id) {
        return userFacade.findUserById(id);
    }

    @PutMapping("/{id}")
    public final UserFullDTO updateUser(@PathVariable Long id, @RequestBody UserFullDTO userFullDTO) {
        userFullDTO.setId(id);
        return userFacade.updateUser(userFullDTO);
    }

    @DeleteMapping("/{id}")
    public final void removeUser(@PathVariable Long id) {
        userFacade.removeUser(id);
    }

}
