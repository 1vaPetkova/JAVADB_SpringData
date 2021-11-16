package com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q05;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerWithPurchasesRootDto {

    @XmlElement(name = "customer")
    private List<CustomerWithPurchasesDto> customers;

    public CustomerWithPurchasesRootDto() {
    }

    public List<CustomerWithPurchasesDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerWithPurchasesDto> customers) {
        this.customers = customers;
    }
}
