package com.example.java_db_06_lab.services;

import com.example.java_db_06_lab.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

public interface AccountService {

    void transferBetweenAccounts(Long from, Long to, BigDecimal amount) throws InsufficientFundsException;

    void withdrawMoney(BigDecimal amount, Long id) throws InsufficientFundsException;

    void transferMoney(BigDecimal amount, Long id);
}
