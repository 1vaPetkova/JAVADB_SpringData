package com.example.java_db_08_exercise.service;

import com.example.java_db_08_exercise.model.dto.UserLoginDto;
import com.example.java_db_08_exercise.model.dto.UserRegisterDto;
import com.example.java_db_08_exercise.model.entities.User;

public interface UserService {


    String registerUser(UserRegisterDto userRegisterDto);

    String loginUser(UserLoginDto userLoginDto);

    String logout();

    boolean isUserLoggedIn();

    User findLoggedInUser();

    String getOwnedGames();
}
