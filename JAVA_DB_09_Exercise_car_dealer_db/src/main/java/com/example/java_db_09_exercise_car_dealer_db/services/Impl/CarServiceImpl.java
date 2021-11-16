package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.CarSeedDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CarViewDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CarsWithPartsDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.PartsViewDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Car;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import com.example.java_db_09_exercise_car_dealer_db.repositories.CarRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.CarService;
import com.example.java_db_09_exercise_car_dealer_db.services.PartService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.CARS_PATH;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final PartService partService;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CarServiceImpl(CarRepository carRepository, PartService partService, Gson gson,
                          FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.partService = partService;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCars() throws IOException {
        if (this.carRepository.count() == 0) {
            Arrays
                    .stream(gson.fromJson(fileUtil.readFileContent(CARS_PATH), CarSeedDto[].class))
                    .filter(validationUtil::isValid)
                    .map(carSeedDto -> {
                        Car car = modelMapper.map(carSeedDto, Car.class);
                        car.setParts(this.partService.findRandomParts());
                        return car;
                    })
                    .forEach(this.carRepository::save);
        }
    }

    @Override
    public Car findRandomCar() {
        long randomId = ThreadLocalRandom
                .current()
                .nextLong(1, this.carRepository.count() + 1);
        return this.carRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<CarViewDto> findAllByMakeOrderByModelAscTravelledDistanceDesc(String make) {
        return this.carRepository
                .findAllByMakeOrderByModelAscTravelledDistanceDesc(make)
                .stream()
                .map(car -> this.modelMapper.map(car, CarViewDto.class))
                .collect(Collectors.toList());

    }

    //Q04
    @Override
    public List<CarsWithPartsDto> findAllCarsWithParts() {
        return this.carRepository
                .findAllBy()
                .stream().map(car -> {
                    CarsWithPartsDto carsWithPartsDto = this.modelMapper.map(car, CarsWithPartsDto.class);
                    Set<PartsViewDto> partsViewDtos = new HashSet<>();
                    for (Part part : car.getParts()) {
                        partsViewDtos.add(this.modelMapper.map(part, PartsViewDto.class));
                    }
                    carsWithPartsDto.setParts(partsViewDtos);
                    return carsWithPartsDto;
                }).collect(Collectors.toList());
    }
}
