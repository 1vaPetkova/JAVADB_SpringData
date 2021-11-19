package softuni.exam.instagraphlite.models.dtos.seed.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PictureSeedDto implements Serializable {

    @Expose
    private String path;
    @Expose
    private Double size;

    @NotBlank
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NotNull
    @Min(value = 500)
    @Max(value = 60000)
    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
