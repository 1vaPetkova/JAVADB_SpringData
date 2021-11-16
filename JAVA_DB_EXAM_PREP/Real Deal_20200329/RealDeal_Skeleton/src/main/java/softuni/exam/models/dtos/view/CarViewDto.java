package softuni.exam.models.dtos.view;

import java.time.LocalDate;

public class CarViewDto {

    private String make;
    private String model;
    private Integer kilometers;
    private LocalDate registeredOn;
    private Integer picturesCount;

    public CarViewDto() {
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

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Integer getPicturesCount() {
        return picturesCount;
    }

    public void setPicturesCount(Integer picturesCount) {
        this.picturesCount = picturesCount;
    }
}
