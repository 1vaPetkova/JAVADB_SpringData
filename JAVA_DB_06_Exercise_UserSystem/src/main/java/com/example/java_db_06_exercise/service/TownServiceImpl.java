package com.example.java_db_06_exercise.service;

import com.example.java_db_06_exercise.repository.TownRepository;
import com.example.java_db_06_exercise.service.interfaces.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;
    }
}
