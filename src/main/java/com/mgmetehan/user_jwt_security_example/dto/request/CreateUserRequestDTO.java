package com.mgmetehan.user_jwt_security_example.dto.request;

import com.mgmetehan.user_jwt_security_example.model.type.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record CreateUserRequestDTO(
        String name,
        String surname,
        String username,
        String password,
        Set<Role> authorities
){
}
