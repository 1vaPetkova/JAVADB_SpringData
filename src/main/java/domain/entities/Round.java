package domain.entities;

import domain.enums.RoundType;

import javax.persistence.*;

@Entity
@Table(name = "rounds")
public class Round extends BaseEntity{

    private RoundType name;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    public RoundType getName() {
        return name;
    }

    public void setName(RoundType name) {
        this.name = name;
    }
}
