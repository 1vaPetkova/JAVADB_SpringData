package mostwanted.domain.dtos;


import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class TownImportDto implements Serializable {

    @Expose
    private String name;

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
