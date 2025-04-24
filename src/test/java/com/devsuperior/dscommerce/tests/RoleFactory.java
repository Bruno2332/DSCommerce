package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.entities.Role;

public class RoleFactory {

    public static Role createRoleAdmin(){
        return new Role(1L, "ROLE_ADMIN");
    }

    public static Role createRoleClient(){
        return new Role(2L, "ROLE_CLIENT");
    }

}
