package com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q06;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleCarCustomerRootDto {

    @XmlElement(name = "sale")
    private List<SaleCarCustomerDto> sales;

    public SaleCarCustomerRootDto() {
    }

    public List<SaleCarCustomerDto> getSales() {
        return sales;
    }

    public void setSales(List<SaleCarCustomerDto> sales) {
        this.sales = sales;
    }
}
