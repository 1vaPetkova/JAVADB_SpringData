package com.example.java_db_08_lab.model.dto;

import java.math.BigDecimal;

public class EmployeeDto extends BasicEmployeeDto {

    private BigDecimal income;

    public String getManagerLastName() {
        return managerLastName;
    }

    public void setManagerLastName(String managerLastName) {
        this.managerLastName = managerLastName;
    }

    private String managerLastName;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
