package com.example.java_db_08_lab.model.dto;

import com.google.gson.annotations.Expose;

public class EmployeeCreateResponse extends EmployeeCreateRequest{

    @Expose
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
