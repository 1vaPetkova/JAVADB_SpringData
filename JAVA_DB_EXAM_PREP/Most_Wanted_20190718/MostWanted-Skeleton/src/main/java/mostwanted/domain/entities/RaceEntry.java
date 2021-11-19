package mostwanted.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "race_entries")
public class RaceEntry extends BaseEntity {
    private Boolean hasFinished;
    private Double finishTime;
    private Car car;
    private Racer racer;
    private Race race;

    @Column(name = "has_finished")
    public Boolean getHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(Boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    @Column(name = "finish_time")
    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    @ManyToOne
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @ManyToOne
    public Racer getRacer() {
        return racer;
    }

    public void setRacer(Racer racer) {
        this.racer = racer;
    }

    @ManyToOne
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
