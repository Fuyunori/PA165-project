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
import tennisclub.entity.User;
import tennisclub.enums.Role;
import tennisclub.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        fullDto = new UserFullDTO();
        fullDto.setUsername(dto.getUsername());
        entity = new User();
        entity.setUsername(dto.getUsername());

        otherDto = new UserDTO();
        otherDto.setUsername("someDifferentUsername");
        otherFullDto = new UserFullDTO();
        otherFullDto.setUsername(otherDto.getUsername());
        otherEntity = new User();
        otherEntity.setUsername(otherDto.getUsername());
    }

    @Test
    void register() {
        ArgumentCaptor<User> passedEntity = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> passedPassword = ArgumentCaptor.forClass(String.class);

        userFacade.register(authDto);
        verify(userService).register(passedEntity.capture(), passedPassword.capture());
        assertThat(passedEntity.getValue().getUsername()).isEqualTo(username);
        assertThat(passedPassword.getValue()).isEqualTo(password);
    }

    @Test
    void authenticate() {
        when(userService.authenticate(authEntity, password)).thenReturn(true);
        ArgumentCaptor<User> passedEntity = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> passedPassword = ArgumentCaptor.forClass(String.class);

        assertThat(userFacade.authenticate(authDto)).isTrue();
        verify(userService).authenticate(passedEntity.capture(), passedPassword.capture());
        assertThat(passedEntity.getValue().getUsername()).isEqualTo(username);
        assertThat(passedPassword.getValue()).isEqualTo(password);
    }

    @Test
    void forwardAuthFailure() {
        when(userService.authenticate(authEntity, password)).thenReturn(false);
        assertThat(userFacade.authenticate(authDto)).isFalse();
    }

    @Test
    void hasRights() {
        when(userService.hasRights(entity, Role.USER)).thenReturn(true);
        assertThat(userFacade.hasRights(dto, Role.USER)).isTrue();

        when(userService.hasRights(entity, Role.USER)).thenReturn(false);
        assertThat(userFacade.hasRights(dto, Role.USER)).isFalse();

        when(userService.hasRights(entity, Role.MANAGER)).thenReturn(true);
        assertThat(userFacade.hasRights(dto, Role.MANAGER)).isTrue();

        when(userService.hasRights(entity, Role.MANAGER)).thenReturn(false);
        assertThat(userFacade.hasRights(dto, Role.MANAGER)).isFalse();
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

        List<UserFullDTO> entities = userFacade.findUsersByName("Karel");
        verify(userService).findUsersByName("Karel");
        assertThat(entities).hasSize(2).contains(fullDto, otherFullDto);
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

        List<UserFullDTO> entities = userFacade.findUsersByEmail("pepa@localhost");
        verify(userService).findUsersByEmail("pepa@localhost");
        assertThat(entities).hasSize(2).contains(fullDto, otherFullDto);
    }

    @Test
    void updateUser() {
        // TODO
    }

    @Test
    void removeUser() {
        // TODO
    }
}
