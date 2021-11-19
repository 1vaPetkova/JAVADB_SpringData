package alararestaurant.domain.dtos.seed.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemsSeedRootDto implements Serializable {

    @XmlElement(name = "item")
    private List<OrderItemsSeedDto> items;

    public OrderItemsSeedRootDto() {
    }


    public List<OrderItemsSeedDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsSeedDto> items) {
        this.items = items;
    }
}
