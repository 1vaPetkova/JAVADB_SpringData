package alararestaurant.domain.dtos.seed.xml;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemsSeedDto implements Serializable {
    @XmlElement
    private String name;
    private Integer quantity;

    public OrderItemsSeedDto() {
    }

    @NotBlank
    @Min(value = 3)
    @Max(value = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Positive
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
