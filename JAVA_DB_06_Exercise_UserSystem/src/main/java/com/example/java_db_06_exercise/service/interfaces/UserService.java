package com.example.java_db_06_exercise.service.interfaces;

import com.example.java_db_06_exercise.model.enitities.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    void persistUser(User user);

    List<String> getUsersByEmailProvider(String provider);

    int removeInactiveUsers(LocalDate date);
}
