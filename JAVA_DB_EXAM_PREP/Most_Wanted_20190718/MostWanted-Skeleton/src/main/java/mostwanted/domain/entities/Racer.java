package mostwanted.domain.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "racers")
public class Racer extends BaseEntity {


    private String name;
    private Integer age;
    private Double bounty;
    private Town homeTown;
    private Set<Car> cars;

    public Racer() {
        this.cars = new HashSet<>();
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(columnDefinition = "DECIMAL(19,2)")
    public Double getBounty() {
        return bounty;
    }

    public void setBounty(Double bounty) {
        this.bounty = bounty;
    }

    @ManyToOne(targetEntity = Town.class)
    public Town getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(Town homeTown) {
        this.homeTown = homeTown;
    }

    @OneToMany(mappedBy = "racer")
    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
}
