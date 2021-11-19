package alararestaurant.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {


    private Integer quantity;
    private Item item;
    private Order order;


    public OrderItem() {
    }

    public OrderItem(Integer quantity, Item item, Order order) {
        this.quantity = quantity;
        this.item = item;
        this.order = order;
    }

    @Column(nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(targetEntity = Item.class,optional = false, cascade = CascadeType.ALL)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @ManyToOne(targetEntity = Order.class,optional = false, cascade = CascadeType.ALL)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
