package com.example.java_db_09_exercise.model.dto;

import com.example.java_db_09_exercise.model.dto.Q04.UserInfoDto;
import com.google.gson.annotations.Expose;

import java.util.Set;

public class UsersWithSoldProductsDto {

    //Q04
    @Expose
    private Integer count;
    @Expose
    private Set<UserInfoDto> users;

    public UsersWithSoldProductsDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount() {
        this.count = this.users.size();
    }

    public Set<UserInfoDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserInfoDto> users) {
        this.users = users;
    }
}
