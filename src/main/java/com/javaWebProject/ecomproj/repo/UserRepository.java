package com.javaWebProject.ecomproj.repo;

import com.javaWebProject.ecomproj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by username
    Optional<User> findByUsername(String username);
}
