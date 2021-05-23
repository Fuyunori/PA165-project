package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.dto.user.UserUpdateDTO;
import tennisclub.enums.Role;
import tennisclub.facade.UserFacade;
import tennisclub.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
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
    public final ResponseEntity<UserFullDTO> register(@Valid @RequestBody UserAuthDTO userAuthDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userFacade.register(userAuthDTO));
    }

    @PostMapping("/login")
    public final ResponseEntity<String> authenticate(@Valid @RequestBody UserAuthDTO userAuthDTO) {
        return ResponseEntity.ok(userFacade.authenticate(userAuthDTO));
    }

    @GetMapping
    public final ResponseEntity<List<UserFullDTO>> findAllUsers(@RequestHeader(value = "Authorization", required = false) String jwt,
                                                                @RequestParam(value = "username", required = false) String username) {
        if(username != null){
            userService.verifyRole(jwt, Role.USER);
            return ResponseEntity.ok(Collections.singletonList(userFacade.findUserByUsername(username)));
        }
        userService.verifyRole(jwt, Role.MANAGER);
        return ResponseEntity.ok(userFacade.findAllUsers());
    }

    @GetMapping("/{id}")
    public final ResponseEntity<UserFullDTO> findUserById(@RequestHeader(value = "Authorization", required = false) String jwt, @PathVariable Long id) {
        userService.verifyUserOrManager(jwt, id);
        return ResponseEntity.ok(userFacade.findUserById(id));
    }

    @PutMapping("/{id}")
    public final ResponseEntity<UserFullDTO> updateUser(
            @RequestHeader(value = "Authorization", required = false) String jwt,
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        userService.verifyUserOrManager(jwt, id);
        return ResponseEntity.ok(userFacade.updateUser(id, userUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<Void> removeUser(@RequestHeader(value = "Authorization", required = false) String jwt, @PathVariable Long id) {
        userService.verifyUserOrManager(jwt, id);
        userFacade.removeUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
