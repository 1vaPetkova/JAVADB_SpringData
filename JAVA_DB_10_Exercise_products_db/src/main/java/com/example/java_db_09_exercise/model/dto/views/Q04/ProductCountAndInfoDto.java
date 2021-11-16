package com.example.java_db_09_exercise.model.dto.views.Q04;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;


@XmlRootElement(name = "sold-products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductCountAndInfoDto {

    //Q04
    @XmlAttribute(name = "count")
    private Integer count;
    @XmlElement(name = "product")
    private Set<ProductNameAndPriceDto> products;

    public ProductCountAndInfoDto() {
        this.products = new HashSet<>();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount() {
        this.count = products.size();
    }

    public Set<ProductNameAndPriceDto> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductNameAndPriceDto> products) {
        this.products = products;
    }
}
