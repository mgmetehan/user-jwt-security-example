package com.mgmetehan.user_jwt_security_example.repository;

import com.mgmetehan.user_jwt_security_example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);
}
