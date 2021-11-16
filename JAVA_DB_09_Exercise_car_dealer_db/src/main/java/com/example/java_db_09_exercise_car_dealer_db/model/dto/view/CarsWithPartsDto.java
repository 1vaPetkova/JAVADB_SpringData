package com.example.java_db_09_exercise_car_dealer_db.model.dto.view;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class CarsWithPartsDto {

    @Expose
    @SerializedName("Make")
    private String make;

    @Expose
    @SerializedName("Model")
    private String model;

    @Expose
    @SerializedName("TravelledDistance")
    private Long travelledDistance;

    @Expose
    private Set<PartsViewDto> parts;

    public CarsWithPartsDto() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public Set<PartsViewDto> getParts() {
        return parts;
    }

    public void setParts(Set<PartsViewDto> parts) {
        this.parts = parts;
    }
}
