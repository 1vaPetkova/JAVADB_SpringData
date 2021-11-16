package com.example.java_db_09_exercise.model.dto.views.Q04;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfoDto {

    @XmlAttribute
    private String firstName;
    @XmlAttribute
    private String lastName;
    @XmlAttribute
    private Integer age;
    @XmlElement(name = "sold-products")
    private ProductCountAndInfoDto soldProducts;

    public UserInfoDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public ProductCountAndInfoDto getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(ProductCountAndInfoDto soldProducts) {
        this.soldProducts = soldProducts;
    }
}
