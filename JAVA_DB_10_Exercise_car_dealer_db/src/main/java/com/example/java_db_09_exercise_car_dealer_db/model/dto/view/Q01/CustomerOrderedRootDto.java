package com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerOrderedRootDto {

    @XmlElement(name = "customer")
    private List<CustomersOrderedDto> customers;

    public List<CustomersOrderedDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomersOrderedDto> customers) {
        this.customers = customers;
    }
}
