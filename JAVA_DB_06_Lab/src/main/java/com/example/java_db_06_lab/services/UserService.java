package com.example.java_db_06_lab.services;

import com.example.java_db_06_lab.exceptions.UserNotFoundException;
import com.example.java_db_06_lab.exceptions.UsernameAlreadyExistsException;

import java.math.BigDecimal;

public interface UserService {

    void register(String username,int age, BigDecimal initialAmount)
            throws UsernameAlreadyExistsException;

    void addAccount(BigDecimal amount, long id) throws UserNotFoundException;
}
