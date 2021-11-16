package com.example.java_db_06_lab.services;

import com.example.java_db_06_lab.exceptions.UsernameAlreadyExistsException;
import com.example.java_db_06_lab.models.Account;
import com.example.java_db_06_lab.models.User;
import com.example.java_db_06_lab.repositories.AccountRepository;
import com.example.java_db_06_lab.repositories.UserRepository;
import com.example.java_db_06_lab.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void register(String username, int age, BigDecimal initialAmount) throws UsernameAlreadyExistsException {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User();
        user.setUsername(username);
        user.setAge(age);
        this.userRepository.save(user);
        saveAccount(initialAmount, user);

    }

    @Override
    public void addAccount(BigDecimal amount, long id) throws UserNotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        saveAccount(amount, user);

    }

    private void saveAccount(BigDecimal initialAmount, User user) {
        Account firstAccount = new Account();
        firstAccount.setBalance(initialAmount);
        firstAccount.setUser(user);
        this.accountRepository.save(firstAccount);
    }
}
