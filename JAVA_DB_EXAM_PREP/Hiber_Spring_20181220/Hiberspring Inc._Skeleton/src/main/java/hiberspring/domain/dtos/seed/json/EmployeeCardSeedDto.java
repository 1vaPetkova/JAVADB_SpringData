package hiberspring.domain.dtos.seed.json;

import com.google.gson.annotations.Expose;

public class EmployeeCardSeedDto {
    @Expose
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
