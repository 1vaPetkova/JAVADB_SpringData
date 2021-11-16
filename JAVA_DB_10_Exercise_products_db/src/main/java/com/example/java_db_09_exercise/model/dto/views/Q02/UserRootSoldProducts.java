package com.example.java_db_09_exercise.model.dto.views.Q02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserRootSoldProducts {

    @XmlElement(name = "user")
    private List<UsersSoldProductsDto> products;

    public List<UsersSoldProductsDto> getProducts() {
        return products;
    }

    public void setProducts(List<UsersSoldProductsDto> products) {
        this.products = products;
    }
}
