package com.example.java_db_06_lab.services;

import com.example.java_db_06_lab.exceptions.InsufficientFundsException;
import com.example.java_db_06_lab.models.Account;
import com.example.java_db_06_lab.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void transferBetweenAccounts(Long from, Long to, BigDecimal amount) throws InsufficientFundsException {
        this.withdrawMoney(amount, from);
        this.transferMoney(amount, to);
    }

    @Override
    public void withdrawMoney(BigDecimal amount, Long id) throws InsufficientFundsException {
        Account account = getAccount(id);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance().subtract(amount));
        this.accountRepository.save(account);
    }

    @Override
    public void transferMoney(BigDecimal amount, Long id) {
        Account account = getAccount(id);
        account.setBalance(account.getBalance().add(amount));
        this.accountRepository.save(account);
    }

    private Account getAccount(Long id) {
        return this.accountRepository.findById(id).orElseThrow();
    }
}
