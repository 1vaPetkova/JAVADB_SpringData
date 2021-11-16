package com.example.java_db_08_exercise.model.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private User buyer;
    private Set<Game> games;
    private Boolean isOrderFinalized;

    public Order() {
        this.games = new HashSet<>();
    }

    public Order(User buyer, Set<Game> games) {
        this.buyer = buyer;
        this.games = games;
        isOrderFinalized = true;
    }

    @ManyToOne
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @ManyToMany(targetEntity = Game.class, fetch = FetchType.EAGER)
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Boolean getOrderFinalized() {
        return isOrderFinalized;
    }

    public void setOrderFinalized(Boolean orderFinalized) {
        isOrderFinalized = orderFinalized;
    }
}
