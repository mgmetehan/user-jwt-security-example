package com.mgmetehan.user_jwt_security_example.service;

import com.mgmetehan.user_jwt_security_example.dto.request.AuthRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String generateToken(AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(request.username());
            } else {
                throw new UsernameNotFoundException("Invalid username or password.");
            }
        } catch (AuthenticationException ex) {
            throw new UsernameNotFoundException("Invalid username or password.", ex);
        }
    }
}
