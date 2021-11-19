package alararestaurant.domain.dtos.seed.xml;

import alararestaurant.domain.entities.enums.OrderType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderSeedDto implements Serializable {

    @XmlElement
    private String customer;
    @XmlElement
    private String employee;
    @XmlElement(name = "date-time")
    private String dateTime;
    @XmlElement
    private OrderType type;
    @XmlElement(name = "items")
    private OrderItemsSeedRootDto orderItems;

    public OrderSeedDto() {
    }

    @NotBlank
    @Size(min = 3, max = 30)
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @NotBlank
    @Size(min = 3, max = 30)
    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @NotBlank
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @NotNull
    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderItemsSeedRootDto getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItemsSeedRootDto orderItems) {
        this.orderItems = orderItems;
    }
}
