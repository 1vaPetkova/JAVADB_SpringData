package com.example.java_db_09_exercise.model.dto.views.Q01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRootNamePriceSellerDto {

    @XmlElement(name = "product")
    private List<ProductNamePriceSellerDto> products;

    public List<ProductNamePriceSellerDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductNamePriceSellerDto> products) {
        this.products = products;
    }
}
