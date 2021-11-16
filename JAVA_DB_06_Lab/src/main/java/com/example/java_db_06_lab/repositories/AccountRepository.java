package com.example.java_db_06_lab.repositories;

import com.example.java_db_06_lab.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

}
