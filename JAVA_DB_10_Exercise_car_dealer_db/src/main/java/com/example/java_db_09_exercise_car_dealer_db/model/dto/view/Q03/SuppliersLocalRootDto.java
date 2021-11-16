package com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q03;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuppliersLocalRootDto {

    @XmlElement(name = "supplier")
    private List<SuppliersLocalDto> suppliers;

    public SuppliersLocalRootDto() {
    }

    public List<SuppliersLocalDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SuppliersLocalDto> suppliers) {
        this.suppliers = suppliers;
    }
}
