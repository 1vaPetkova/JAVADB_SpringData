package com.example.java_db_08_lab.model.dto;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "managers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ManagerCollection {

    @XmlElement(name = "manager")
    @Expose
    private List<ManagerDto> managers;

    public ManagerCollection(List<ManagerDto> managers) {
        this.managers = managers;
    }

    public ManagerCollection() {
    }

    public List<ManagerDto> getManagers() {
        return managers;
    }

    public void setManagers(List<ManagerDto> managers) {
        this.managers = managers;
    }
}
