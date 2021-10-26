package domain.entities;

import domain.enums.CompetitionTypes;

import javax.persistence.*;

@Entity
@Table(name = "competition_type")
public class CompetitionType extends BaseEntity {

    private CompetitionTypes type;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public CompetitionTypes getName() {
        return type;
    }

    public void setName(CompetitionTypes type) {
        this.type = type;
    }
}
