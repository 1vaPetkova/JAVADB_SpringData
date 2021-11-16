package com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q04;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarsWithPartsRootDto {

    @XmlElement(name = "car")
    private List<CarsWithPartsDto> cars;

    public CarsWithPartsRootDto() {
    }

    public List<CarsWithPartsDto> getCars() {
        return cars;
    }

    public void setCars(List<CarsWithPartsDto> cars) {
        this.cars = cars;
    }
}
