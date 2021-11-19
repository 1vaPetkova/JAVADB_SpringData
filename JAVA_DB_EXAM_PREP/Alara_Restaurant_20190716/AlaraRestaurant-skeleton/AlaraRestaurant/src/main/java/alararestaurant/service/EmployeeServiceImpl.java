package alararestaurant.service;

import alararestaurant.domain.dtos.seed.json.EmployeeSeedDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.util.files.FilePaths;
import alararestaurant.util.files.FileUtil;
import alararestaurant.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository,
                               FileUtil fileUtil, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {
        return this.fileUtil.readFile(FilePaths.EMPLOYEES_PATH);
    }

    @Override
    public String importEmployees(String employees) {
        StringBuilder sb = new StringBuilder();
        EmployeeSeedDto[] employeeSeedDtos = gson.fromJson(employees, EmployeeSeedDto[].class);

        Arrays
                .stream(employeeSeedDtos)
                .filter(employeeSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(employeeSeedDto);
                    sb
                            .append(isValid
                                    ? getSuccessfulMessage(employeeSeedDto)
                                    : "Invalid data format.")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(employeeSeedDto -> {
                    Position position = this.positionRepository.findByName(employeeSeedDto.getPosition());
                    if (position == null) {
                        position = new Position(employeeSeedDto.getPosition());
                        position = this.positionRepository.save(position);
                    }
                    Employee employee = this.modelMapper.map(employeeSeedDto, Employee.class);
                    employee.setPosition(position);
                    return employee;
                })
                .forEach(this.employeeRepository::save);
        return sb.toString();
    }

    private String getSuccessfulMessage(EmployeeSeedDto employeeSeedDto) {
        return String.format("Record %s successfully imported.", employeeSeedDto.getName());
    }
}
