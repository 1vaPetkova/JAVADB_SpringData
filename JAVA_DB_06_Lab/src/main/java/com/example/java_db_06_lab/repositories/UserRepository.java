package com.example.java_db_06_lab.repositories;

import com.example.java_db_06_lab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
}
