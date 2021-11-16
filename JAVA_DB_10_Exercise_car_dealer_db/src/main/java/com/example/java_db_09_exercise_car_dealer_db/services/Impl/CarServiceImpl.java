package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.CarSeedRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q02.CarViewDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q02.CarViewRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q04.CarsWithPartsDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q04.CarsWithPartsRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q04.PartsViewDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Car;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import com.example.java_db_09_exercise_car_dealer_db.repositories.CarRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.CarService;
import com.example.java_db_09_exercise_car_dealer_db.services.PartService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.CARS_PATH;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final PartService partService;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;

    public CarServiceImpl(CarRepository carRepository, PartService partService,
                          FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper, XMLParser xmlParser) {
        this.carRepository = carRepository;
        this.partService = partService;

        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void seedCars() throws IOException, JAXBException {
        if (this.carRepository.count() == 0) {
            CarSeedRootDto carSeedRootDto = this.xmlParser.fromFile(CARS_PATH, CarSeedRootDto.class);
            carSeedRootDto.getCars()
                    .stream()
                    .filter(this.validationUtil::isValid)
                    .map(carSeedDto -> {
                        Car car = this.modelMapper.map(carSeedDto, Car.class);
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
    public CarViewRootDto findAllByMakeOrderByModelAscTravelledDistanceDesc(String make) {
        CarViewRootDto carViewRootDto = new CarViewRootDto();
        carViewRootDto.setCars(this.carRepository
                .findAllByMakeOrderByModelAscTravelledDistanceDesc(make)
                .stream()
                .map(car -> this.modelMapper.map(car, CarViewDto.class))
                .collect(Collectors.toList()));
        return carViewRootDto;
    }

    //Q04
    @Override
    public CarsWithPartsRootDto findAllCarsWithParts() {
        CarsWithPartsRootDto carsWithPartsRootDto = new CarsWithPartsRootDto();
        carsWithPartsRootDto.setCars(this.carRepository
                .findAllBy()
                .stream().map(car -> {
                    CarsWithPartsDto carsWithPartsDto = this.modelMapper.map(car, CarsWithPartsDto.class);
                    Set<PartsViewDto> partsViewDtos = new HashSet<>();
                    for (Part part : car.getParts()) {
                        partsViewDtos.add(this.modelMapper.map(part, PartsViewDto.class));
                    }
                    carsWithPartsDto.setParts(partsViewDtos);
                    return carsWithPartsDto;
                }).collect(Collectors.toList()));
        return carsWithPartsRootDto;
    }
}
