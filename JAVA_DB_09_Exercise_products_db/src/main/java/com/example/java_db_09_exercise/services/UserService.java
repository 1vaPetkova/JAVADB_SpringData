package com.example.java_db_09_exercise.services;

import com.example.java_db_09_exercise.model.dto.Q04.SellersByCountDto;
import com.example.java_db_09_exercise.model.dto.UsersSoldProductsDto;
import com.example.java_db_09_exercise.model.entities.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    User findRandomUser();

    //Q02
    List<UsersSoldProductsDto> findAllUsersByMoreThanOneSoldProductsOrderByLastNameThenFirstName();

    //Q04
    SellersByCountDto findAllUsersWithSoldProductsOrderByProductsCountThenByLastName();
}
