package com.example.java_db_08_lab;

import com.example.java_db_08_lab.model.dto.EmployeeDto;
import com.example.java_db_08_lab.model.dto.ManagerDto;
import com.example.java_db_08_lab.model.entities.Employee;
import com.example.java_db_08_lab.services.EmployeeService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

@Component
public class App implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;
    private final BufferedReader reader;


    public App(EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Enter problem number: ");
        int problem = Integer.parseInt(reader.readLine());

        switch (problem) {
            case 1:
                //1.Create address and employee and map it to EmployeeDto
                createAddressAndEmployee();
                break;
            case 2:
                //2. Advanced Mapping - type map, mapping addresses
                // and employees to ManagerDto with EmployeeDto as subordinates
                typeMap();
                break;
            case 3:
                // 3. Employees born after 1990 with manager last name
                employeeBornAfter1990();
                break;
        }
    }

    private void employeeBornAfter1990() {
        // 3. Employees born after 1990 with manager last Name
        Converter<String, String> converterNoManager = ctx ->
                ctx.getSource() == null ? "[No manager]" : ctx.getSource();

        TypeMap<Employee, EmployeeDto> employeeMap = modelMapper
                .getTypeMap(Employee.class, EmployeeDto.class)
                .addMappings(m -> m.using(converterNoManager)
                        .map(src -> src.getManager().getLastName(), EmployeeDto::setLastName));
        List<Employee> employeesBefore1990 = this.employeeService
                .findAllByBirthdayBeforeOrderBySalaryDesc(LocalDate.of(1990, 1, 1));
        employeesBefore1990.stream().map(employeeMap::map).forEach(e->
                System.out.printf("%s %s %.2f – Manager: %s\n",
                        e.getFirstName(),e.getLastName(),e.getIncome(),e.getManagerLastName()));
    }

    //2. Advanced Mapping - type map, mapping addresses
    // and employees to ManagerDto with EmployeeDto as subordinates
    private void typeMap() {
        ManagerDto managerDto = this.employeeService.findManager(1L);
        System.out.println(managerDto.getFirstName() + " " + managerDto.getLastName() +
                " | Employees: " + managerDto.getSubordinates().size());
        managerDto.getSubordinates().forEach(e ->
                System.out.println("    - " + e.getFirstName() + " " + e.getLastName() + " " + e.getIncome()));
        System.out.println("==========================================================================");

        List<ManagerDto> managers = this.employeeService.findAll();
        managers.forEach(mDto -> {
            System.out.println(mDto.getFirstName() + " " + mDto.getLastName() +
                    " | Employees: " + mDto.getSubordinates().size());
            mDto.getSubordinates().forEach(e ->
                    System.out.println("    - " + e.getFirstName() + " " + e.getLastName() + " " + e.getIncome()));
        });
        System.out.println("==========================================================================");
    }

    //1.Create address and employee and map it to EmployeeDto
    private void createAddressAndEmployee() {
        EmployeeDto dto = this.employeeService.findOne(2L);
        System.out.println(dto.getLastName() + " " + dto.getLastName() + "; salary: " + dto.getIncome());
        System.out.println("==========================================================================");

    }
}

