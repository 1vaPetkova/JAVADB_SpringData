package com.example.java_db_09_exercise.services;

import com.example.java_db_09_exercise.model.dto.views.Q02.UserRootSoldProducts;
import com.example.java_db_09_exercise.model.dto.views.Q04.UsersWithSoldProductsRootDto;
import com.example.java_db_09_exercise.model.entities.User;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface UserService {
    void seedUsers() throws IOException, JAXBException;

    User findRandomUser();

    //Q02
    UserRootSoldProducts findAllUsersByMoreThanOneSoldProductsOrderByLastNameThenFirstName();

    //Q04
    UsersWithSoldProductsRootDto findAllUsersWithSoldProductsOrderByProductsCountThenByLastName();
}
