package mostwanted.domain.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class DistrictImportDto implements Serializable {
    @Expose
    private String name;
    @Expose
    @SerializedName("townName")
    private String town;

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
