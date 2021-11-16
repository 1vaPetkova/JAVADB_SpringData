package com.example.java_db_09_exercise.model.dto.views.Q04;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersWithSoldProductsRootDto {

    @XmlAttribute
    private Integer count;
    @XmlElement(name = "user")
    private List<UserInfoDto> users;

    public UsersWithSoldProductsRootDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount() {
        this.count = this.users.size();
    }

    public List<UserInfoDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDto> users) {
        this.users = users;
    }
}
