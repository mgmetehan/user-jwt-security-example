package com.mgmetehan.user_jwt_security_example.controller;

import com.mgmetehan.user_jwt_security_example.dto.request.AuthRequestDTO;
import com.mgmetehan.user_jwt_security_example.dto.request.CreateUserRequestDTO;
import com.mgmetehan.user_jwt_security_example.model.User;
import com.mgmetehan.user_jwt_security_example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Hello World!";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<User> addUser(@RequestBody CreateUserRequestDTO request) {
        User createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/generateToken")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequestDTO request) {
        try {
            String token = userService.generateToken(request);
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserString() {
        return ResponseEntity.ok("This is USER!");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdminString() {
        return ResponseEntity.ok("This is ADMIN!");
    }
}
