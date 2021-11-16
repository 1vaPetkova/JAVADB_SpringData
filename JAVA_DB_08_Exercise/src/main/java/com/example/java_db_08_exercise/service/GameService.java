package com.example.java_db_08_exercise.service;

import com.example.java_db_08_exercise.model.dto.GameAddDto;

public interface GameService {
    String addGame(GameAddDto gameAddDto);


    String editGame(long id, String[] values);

    String deleteGame(long parseLong);

    String getAllGames();

    String getDetailsForGame(String token);
}
