package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserService userService;

    private User adminUser;
    private User clientUser;
    private Long nonExistsId;

    @BeforeEach
    void setUp() throws Exception{
        adminUser = UserFactory.createUserAdmin();
        clientUser = UserFactory.createUserClient();
        nonExistsId = 3L;

        when(userService.authenticated()).thenReturn(clientUser);
    }

    @Test
    public void validateSelfOrAdminShouldDoNotThrowsExceptionWhenClientUserValid(){

        Assertions.assertDoesNotThrow(() ->
                service.validateSelfOrAdmin(clientUser.getId()));
    }

    @Test
    public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenDoesNotUserValid(){

        Assertions.assertThrows(ForbiddenException.class, () -> {
            service.validateSelfOrAdmin(nonExistsId);
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoesNotThrowExceptionWhenAdminUser(){
        when(userService.authenticated()).thenReturn(adminUser);

        Assertions.assertDoesNotThrow(() -> {
            service.validateSelfOrAdmin(nonExistsId);
                });
    }



}
