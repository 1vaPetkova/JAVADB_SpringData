package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.CarImportDto;
import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.Racer;
import mostwanted.repository.CarRepository;
import mostwanted.repository.RacerRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static mostwanted.common.Constants.*;

@Service
public class CarServiceImpl implements CarService {

    private final static String CARS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/cars.json";
    private final CarRepository carRepository;
    private final RacerRepository racerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final FileUtil fileUtil;

    public CarServiceImpl(CarRepository carRepository, RacerRepository racerRepository,
                          ValidationUtil validationUtil, ModelMapper modelMapper,
                          Gson gson, FileUtil fileUtil) {
        this.carRepository = carRepository;
        this.racerRepository = racerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }


    @Override
    public Boolean carsAreImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsJsonFile() throws IOException {
        return this.fileUtil.readFile(CARS_JSON_FILE_PATH);
    }

    @Override
    public String importCars(String carsFileContent) {
        StringBuilder sb = new StringBuilder();
        CarImportDto[] carImportDtos = this.gson.fromJson(carsFileContent, CarImportDto[].class);
        for (CarImportDto carImportDto : carImportDtos) {
            if (!this.validationUtil.isValid(carImportDto)) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Racer racer = this.racerRepository.findByName(carImportDto.getRacer());
            if (racer == null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Car car = this.modelMapper.map(carImportDto, Car.class);
            if (this.carRepository
                    .findByBrandAndModelAndYearOfProduction(
                            car.getBrand(), car.getModel(), car.getYearOfProduction()) != null) {
                getDuplicateMessage(sb);
            }
            car.setRacer(racer);
            this.carRepository.saveAndFlush(car);
            getSuccessMessage(sb,
                    Car.class.getSimpleName(),
                    String.format("%s %s @ %d",car.getBrand(), car.getModel(),car.getYearOfProduction()));
        }
        return sb.toString().trim();
    }
}
