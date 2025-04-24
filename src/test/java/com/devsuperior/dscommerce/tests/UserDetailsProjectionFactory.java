package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.projection.UserDetailsProjection;

public class UserDetailsProjectionFactory {

    public static UserDetailsProjection createUserDetailsProjection(){
        return new UserDetailsProjection() {
            @Override
            public String getUsername() {
                return "bruno@gmail.com";
            }

            @Override
            public String getPassword() {
                return "123456";
            }

            @Override
            public Long getRoleId() {
                return 1L;
            }

            @Override
            public String getAuthority() {
                return "ROLE_ADMIN";
            }
        };
    }
}
