package com.example.java_db_09_exercise.model.dto.views.Q03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryRootDto {

    @XmlElement(name = "category")
    private List<CategoryProductsDto> categories;

    public List<CategoryProductsDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryProductsDto> categories) {
        this.categories = categories;
    }
}
