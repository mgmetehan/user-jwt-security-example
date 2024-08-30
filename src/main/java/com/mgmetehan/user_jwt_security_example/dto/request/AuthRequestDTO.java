package com.mgmetehan.user_jwt_security_example.dto.request;

public record AuthRequestDTO(
        String username,
        String password
){
}
