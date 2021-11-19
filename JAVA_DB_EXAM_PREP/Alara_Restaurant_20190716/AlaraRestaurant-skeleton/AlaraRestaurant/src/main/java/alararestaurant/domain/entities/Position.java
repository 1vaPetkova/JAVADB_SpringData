package alararestaurant.domain.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "positions")
public class Position extends BaseEntity {

    private String name;
    private Set<Employee> employees;

    public Position() {
        this.employees = new HashSet<>();
    }

    public Position(String name) {
        this.name = name;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "position", fetch = FetchType.EAGER)
    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
