package com.example.java_db_06_exercise.repository;

import com.example.java_db_06_exercise.model.enitities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAllByEmailEndingWith(String provider);
    List<User> findAllByLastTimeLoggedInBefore(LocalDate date);
 List<User> findAllByDeletedTrue();

}
