package entities;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "plate_numbers")
public class PlateNumber {

    @Id
    private Long id;

    private String number;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private Car car;

    public PlateNumber() {
    }

    public PlateNumber(String number, Car car) {
        this.number = number;
        this.car = car;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlateNumber)) return false;
        PlateNumber that = (PlateNumber) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlateNumber{");
        sb.append("id=").append(id);
        sb.append(", number='").append(number).append('\'');
//        sb.append(", car=").append(car);
        sb.append('}');
        return sb.toString();
    }
}
