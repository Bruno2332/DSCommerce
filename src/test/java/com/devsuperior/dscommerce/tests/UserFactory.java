package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.DTO.UserDTO;
import com.devsuperior.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createUserAdmin(){
        User user = new User(1L, "Bruno", "bruno@gmail.com", "111111111", LocalDate.parse("1980-04-23"), "123456");
        user.addRole(RoleFactory.createRoleAdmin());
        return user;
    }

    public static User createUserClient(){
        User user = new User(2L, "Bruno", "bruno@gmail.com", "111111111", LocalDate.parse("1980-04-23"), "123456");
        user.addRole(RoleFactory.createRoleClient());
        return user;
    }


    public static UserDTO createUserDTO(User entity){
        return new UserDTO(entity);
    }
}
