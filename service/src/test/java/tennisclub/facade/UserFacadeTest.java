package tennisclub.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dto.user.UserAuthDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.dto.user.UserUpdateDTO;
import tennisclub.entity.Court;
import tennisclub.entity.User;
import tennisclub.enums.Role;
import tennisclub.exceptions.UnauthorisedException;
import tennisclub.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pavel Tobiáš
 */
@SpringBootTest
public class UserFacadeTest {
    @MockBean
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    private String username;
    private String password;
    private UserAuthDTO authDto;
    private User authEntity;

    private UserDTO dto;
    private UserFullDTO fullDto;
    private User entity;

    private UserDTO otherDto;
    private UserFullDTO otherFullDto;
    private User otherEntity;

    private UserUpdateDTO updateDto;

    @BeforeEach
    void init() {
        username = "us3rn4m3";
        password = "p4ssw0rd";
        authDto = new UserAuthDTO();
        authDto.setUsername(username);
        authDto.setPassword(password);
        authEntity = new User();
        authEntity.setUsername(authDto.getUsername());

        dto = new UserDTO();
        dto.setUsername("someUsername");
        dto.setId(1L);
        fullDto = new UserFullDTO();
        fullDto.setUsername(dto.getUsername());
        entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setId(dto.getId());

        otherDto = new UserDTO();
        otherDto.setUsername("someDifferentUsername");
        otherFullDto = new UserFullDTO();
        otherFullDto.setUsername(otherDto.getUsername());
        otherEntity = new User();
        otherEntity.setUsername(otherDto.getUsername());

        updateDto = new UserUpdateDTO();
        updateDto.setUsername(dto.getUsername());
    }

    @Test
    void register() {
        ArgumentCaptor<User> passedEntity = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> passedPassword = ArgumentCaptor.forClass(String.class);

        when(userService.register(passedEntity.capture(),passedPassword.capture())).thenReturn(authEntity);

        userFacade.register(authDto);
        verify(userService).register(passedEntity.capture(), passedPassword.capture());
        assertThat(passedEntity.getValue().getUsername()).isEqualTo(username);
        assertThat(passedPassword.getValue()).isEqualTo(password);
    }

    @Test
    void authenticate() {
        when(userService.authenticateJWT(authEntity.getUsername(), password)).thenReturn("token");
        ArgumentCaptor<String> passedUsername = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passedPassword = ArgumentCaptor.forClass(String.class);

        assertThat(userFacade.authenticate(authDto)).isEqualTo("token");
        verify(userService).authenticateJWT(passedUsername.capture(), passedPassword.capture());
        assertThat(passedUsername.getValue()).isEqualTo(username);
        assertThat(passedPassword.getValue()).isEqualTo(password);
    }

    @Test
    void forwardAuthFailure() {
        when(userService.authenticateJWT(authEntity.getUsername(), password)).thenThrow(new UnauthorisedException());
        assertThatThrownBy( () ->
            userFacade.authenticate(authDto)
        ).isInstanceOf(UnauthorisedException.class);
    }

    @Test
    void findAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(entity, otherEntity));

        List<UserFullDTO> entities = userFacade.findAllUsers();
        verify(userService).getAllUsers();
        assertThat(entities).hasSize(2).contains(fullDto, otherFullDto);
    }

    @Test
    void findUserById() {
        when(userService.findUserById(1L)).thenReturn(entity);

        UserFullDTO foundDto = userFacade.findUserById(1L);
        verify(userService).findUserById(1L);
        assertThat(foundDto).isEqualTo(fullDto);
    }

    @Test
    void findUsersByName() {
        when(userService.findUsersByName("Karel")).thenReturn(List.of(entity, otherEntity));

        List<UserFullDTO> foundDtos = userFacade.findUsersByName("Karel");
        verify(userService).findUsersByName("Karel");
        assertThat(foundDtos).hasSize(2).contains(fullDto, otherFullDto);
    }

    @Test
    void findUserByUsername() {
        String username = entity.getUsername();
        when(userService.findUserByUsername(username)).thenReturn(entity);

        UserFullDTO foundDto = userFacade.findUserByUsername(username);
        verify(userService).findUserByUsername(username);
        assertThat(foundDto).isEqualTo(fullDto);
    }

    @Test
    void findUsersByEmail() {
        when(userService.findUsersByEmail("pepa@localhost")).thenReturn(List.of(entity, otherEntity));

        List<UserFullDTO> foundDtos = userFacade.findUsersByEmail("pepa@localhost");
        verify(userService).findUsersByEmail("pepa@localhost");
        assertThat(foundDtos).hasSize(2).contains(fullDto, otherFullDto);
    }

    @Test
    void updateUser() {
        when(userService.updateUserData(entity)).thenReturn(otherEntity);
        ArgumentCaptor<User> passedEntity = ArgumentCaptor.forClass(User.class);

        UserFullDTO updatedUser = userFacade.updateUser(1L, updateDto);
        verify(userService).updateUserData(passedEntity.capture());
        assertThat(passedEntity.getValue()).isEqualTo(entity);
        assertThat(updatedUser).isEqualTo(otherFullDto);
    }

    @Test
    void removeUser() {
        Long passedId = dto.getId();

        when(userService.findUserById(passedId)).thenReturn(entity);

        userFacade.removeUser(passedId);
        verify(userService).findUserById(passedId);
        verify(userService).removeUser(entity);
    }
}
