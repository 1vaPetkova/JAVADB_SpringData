package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.seed.CarSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.files.FileUtilImpl;
import softuni.exam.util.validator.ValidationUtil;

import java.io.IOException;
import java.util.Arrays;

import static softuni.exam.util.files.FilePaths.CARS_PATH;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final Gson gson;
    private final FileUtilImpl fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CarServiceImpl(CarRepository carRepository, Gson gson, FileUtilImpl fileUtil,
                          ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return fileUtil.readFileContent(CARS_PATH);
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder sb = new StringBuilder();
        CarSeedDto[] carSeedDtos = gson.fromJson(readCarsFileContent(), CarSeedDto[].class);
        Arrays
                .stream(carSeedDtos)
                .filter(carSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(carSeedDto);
                    sb
                            .append(isValid ?
                                    getSuccessMessage(carSeedDto) :
                                    "Invalid car")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(carSeedDto -> this.modelMapper.map(carSeedDto, Car.class))
                .forEach(this.carRepository::save);
        return sb.toString();
    }

    private String getSuccessMessage(CarSeedDto carSeedDto) {
        return String.format("Successfully imported car - %s - %s", carSeedDto.getMake(), carSeedDto.getModel());
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder sb = new StringBuilder();
        this.carRepository.findAllByOrderByPicturesCountThenMake()
                .forEach(c->{
                    sb
                            .append(String.format("Car make - %s, model - %s",c.getMake(), c.getModel()))
                            .append(System.lineSeparator())
                            .append(String.format("\tKilometers - %d",c.getKilometers()))
                            .append(System.lineSeparator())
                            .append(String.format("\tRegistered on - %s",c.getRegisteredOn()))
                            .append(System.lineSeparator())
                            .append(String.format("\tNumber of pictures - %d",c.getPictures().size()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }

    @Override
    public Car findById(Long id) {
        return this.carRepository.findById(id).orElse(null);
    }

}
