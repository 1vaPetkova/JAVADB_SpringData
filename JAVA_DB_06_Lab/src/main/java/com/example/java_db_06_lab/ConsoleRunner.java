package com.example.java_db_06_lab;

import com.example.java_db_06_lab.exceptions.InsufficientFundsException;
import com.example.java_db_06_lab.services.AccountService;
import com.example.java_db_06_lab.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConsoleRunner implements CommandLineRunner {

    private final UserService userService;
    private final AccountService accountService;

    public ConsoleRunner(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }


    @Override
    public void run(String... args) throws Exception {
//        try {
//            this.userService.register("Pesho", 25, new BigDecimal(1000));
//        } catch (UsernameAlreadyExistsException e) {
//            System.out.println(e.getClass().getSimpleName());
//        }
      //  this.userService.addAccount(new BigDecimal(750), 1);

        //

//        try {
//            this.accountService.withdrawMoney(new BigDecimal(1200), 1L);
//        } catch (InsufficientFundsException e) {
//            System.out.println(e.getClass().getSimpleName());
//        }

   //     this.accountService.transferMoney(new BigDecimal(200), 2L);


        try {
            this.accountService.transferBetweenAccounts(2L,3L,new BigDecimal(1025));
        } catch (InsufficientFundsException e) {
            e.printStackTrace();
        }
    }
}
