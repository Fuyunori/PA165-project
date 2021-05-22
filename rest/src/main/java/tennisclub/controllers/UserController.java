package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.dto.user.UserUpdateDTO;
import tennisclub.enums.Role;
import tennisclub.facade.UserFacade;
import tennisclub.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for manipulation with users
 * @author Ondrej Holub
 */
@CrossOrigin
@RestController
@RequestMapping("/rest/users")
public class UserController {

    private final UserFacade userFacade;
    private final UserService userService;

    @Autowired
    public UserController(UserFacade userFacade, UserService userService) {
        this.userFacade = userFacade;
        this.userService = userService;
    }

    @PostMapping
    public final UserFullDTO register(@Valid @RequestBody UserAuthDTO userAuthDTO) {
        return userFacade.register(userAuthDTO);
    }

    @PostMapping("/login")
    public final String authenticate(@Valid @RequestBody UserAuthDTO userAuthDTO) {
        return userFacade.authenticate(userAuthDTO);
    }

    @GetMapping
    public final List<UserFullDTO> findAllUsers(@RequestHeader(value = "Authorization", required = false) String jwt) {
        userService.verifyRole(jwt, Role.MANAGER);
        return userFacade.findAllUsers();
    }

    @GetMapping("/{id}")
    public final UserFullDTO findUserById(@RequestHeader(value = "Authorization", required = false) String jwt, @PathVariable Long id) {
        userService.verifyUserOrManager(jwt, id);
        return userFacade.findUserById(id);
    }

    @PutMapping("/{id}")
    public final UserFullDTO updateUser(
            @RequestHeader(value = "Authorization", required = false) String jwt,
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        userService.verifyUserOrManager(jwt, id);
        return userFacade.updateUser(id, userUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public final void removeUser(@RequestHeader(value = "Authorization", required = false) String jwt, @PathVariable Long id) {
        userService.verifyUserOrManager(jwt, id);
        userFacade.removeUser(id);
    }

}
