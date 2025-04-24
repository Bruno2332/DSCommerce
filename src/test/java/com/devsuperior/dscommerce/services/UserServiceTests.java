package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.DTO.UserDTO;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projection.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.UserDetailsProjectionFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import com.devsuperior.dscommerce.util.CustomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private CustomUtil customUtil;

    @Mock
    private SecurityContext securityContext;

    private List<UserDetailsProjection> list;
    private User user;
    private String usernameExists;
    private String usernameNonExists;
    private UserDetailsProjection userDetailsProjection;

    @BeforeEach
    void setUp() throws Exception{

        SecurityContextHolder.setContext(securityContext);
        user = UserFactory.createUserAdmin();
        userDetailsProjection = UserDetailsProjectionFactory.createUserDetailsProjection();
        list = new ArrayList<>();
        list.add(userDetailsProjection);
        usernameExists = "bruno@gmail.com";
        usernameNonExists = "maria@gmail.com";


        when(repository.searchUserAndRolesByEmail(usernameExists)).thenReturn(list);
        when(repository.searchUserAndRolesByEmail(usernameNonExists)).thenReturn(new ArrayList<>());
        when(repository.findByEmail(usernameExists)).thenReturn(Optional.of(user));
        when(repository.findByEmail(usernameNonExists)).thenReturn(Optional.empty());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserWhenUsernameExists(){
        UserDetails result = service.loadUserByUsername(usernameExists);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), list.get(0).getUsername());
    }

    @Test
    public void loadUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameNonExists(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(usernameNonExists);
        });
    }

    @Test
    public void authenticatedShouldReturnUserWhenUserAuthenticaded(){
        when(customUtil.getLoggedUsername()).thenReturn(usernameExists);
        User result = service.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(usernameExists, result.getEmail());
    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUsernameNonExists(){
        when(customUtil.getLoggedUsername()).thenReturn(usernameNonExists);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.authenticated();
        });
    }

    @Test
    public void getMeShouldReturnUserDtoWhenAuthenticatedUser(){
        UserService spyUserService = spy(service);
        doReturn(user).when(spyUserService).authenticated();
        UserDTO dto = spyUserService.getMe();

        Assertions.assertEquals(user.getEmail(), dto.getEmail());
    }

}
