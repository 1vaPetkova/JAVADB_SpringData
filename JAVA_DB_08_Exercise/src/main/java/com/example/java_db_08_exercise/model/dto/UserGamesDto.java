package com.example.java_db_08_exercise.model.dto;

import java.util.Set;

public class UserGamesDto {

    private Set<String> titles;

    public Set<String> titles() {
        return titles;
    }

    public UserGamesDto() {
    }

    public Set<String> getTitles() {
        return titles;
    }

    public void setTitles(Set<String> titles) {
        this.titles = titles;
    }
}
