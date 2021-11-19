package alararestaurant.domain.dtos.seed.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

public class ItemSeedDto implements Serializable {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;
    @Expose
    private String category;


    public ItemSeedDto() {
    }

    @Size(min = 3, max = 30)
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PositiveOrZero
    @NotNull
    @DecimalMin(value = "0.01")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Size(min = 3, max = 30)
    @NotBlank
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
