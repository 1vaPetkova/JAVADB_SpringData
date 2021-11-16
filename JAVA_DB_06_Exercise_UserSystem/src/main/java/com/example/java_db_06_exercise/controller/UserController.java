package com.example.java_db_06_exercise.controller;

import com.example.java_db_06_exercise.model.enitities.User;
import com.example.java_db_06_exercise.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class UserController implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
      //  addUser();
        printAllUsersByEmailProvider("abv.bg");
        removeInactiveUsers(LocalDate.of(2003, 10, 12));
    }


    private void addUser() {
        User user = new User();
        user.setUsername("IkaPoka");
        user.setPassword("aa@bbbbbccccc");
        user.setEmail("ivka@abv.bg.com");
        user.setAge(7);
        userService.persistUser(user);
    }

    private void printAllUsersByEmailProvider(String provider) {
        List<String> users = this.userService.getUsersByEmailProvider(provider);
        System.out.println(users.isEmpty() ? "No users found with email domain abv.bg" :
                String.join(System.lineSeparator(), users));
    }

    public void removeInactiveUsers(LocalDate date) {
        System.out.println(this.userService.removeInactiveUsers(date));

    }
}
