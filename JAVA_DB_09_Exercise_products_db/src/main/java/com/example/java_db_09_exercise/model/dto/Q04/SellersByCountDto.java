package com.example.java_db_09_exercise.model.dto.Q04;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class SellersByCountDto {

    @Expose
    private Integer usersCount;
    @Expose
    private Set<UserInfoDto> users;

    public SellersByCountDto() {
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount() {
        this.usersCount = this.users.size();
    }

    public Set<UserInfoDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserInfoDto> users) {
        this.users = users;
    }
}
