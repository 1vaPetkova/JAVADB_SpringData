package alararestaurant.domain.dtos.seed.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;
import java.io.Serializable;

public class EmployeeSeedDto implements Serializable {

    @Expose
    private String name;
    @Expose
    private Integer age;
    @Expose
    private String position;

    public EmployeeSeedDto() {
    }

    @Size(min = 3, max = 30)
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 15)
    @Max(value = 80)
    @NotNull
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Size(min = 3, max = 30)
    @NotBlank
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
