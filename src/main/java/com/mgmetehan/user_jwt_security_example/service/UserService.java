package com.mgmetehan.user_jwt_security_example.service;

import com.mgmetehan.user_jwt_security_example.dto.request.AuthRequestDTO;
import com.mgmetehan.user_jwt_security_example.dto.request.CreateUserRequestDTO;
import com.mgmetehan.user_jwt_security_example.model.User;
import com.mgmetehan.user_jwt_security_example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User createUser(CreateUserRequestDTO request) {

        User newUser = User.builder()
                .name(request.name())
                .surname(request.surname())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .authorities(request.authorities())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .build();
        return userRepository.save(newUser);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

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
