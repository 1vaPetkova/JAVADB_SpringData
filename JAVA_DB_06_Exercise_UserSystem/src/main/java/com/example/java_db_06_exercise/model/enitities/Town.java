package com.example.java_db_06_exercise.model.enitities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    private String name;
    private Country country;
    private Set<User> born;
    private Set<User> currentlyLivingIn;


    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToOne(targetEntity = Country.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @OneToMany(mappedBy = "bornTown")
    public Set<User> getBorn() {
        return born;
    }

    public void setBorn(Set<User> born) {
        this.born = born;
    }

    @OneToMany(mappedBy = "currentlyLivingInTown")
    public Set<User> getCurrentlyLivingIn() {
        return currentlyLivingIn;
    }

    public void setCurrentlyLivingIn(Set<User> currentlyLivingIn) {
        this.currentlyLivingIn = currentlyLivingIn;
    }
}
