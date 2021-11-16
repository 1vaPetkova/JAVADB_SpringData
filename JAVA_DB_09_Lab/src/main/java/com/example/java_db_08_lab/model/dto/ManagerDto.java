package com.example.java_db_08_lab.model.dto;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class ManagerDto extends BasicEmployeeDto {

    @Expose
    private Set<EmployeeDto> subordinates;

    public Set<EmployeeDto> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<EmployeeDto> subordinates) {
        this.subordinates = subordinates;
    }


}


