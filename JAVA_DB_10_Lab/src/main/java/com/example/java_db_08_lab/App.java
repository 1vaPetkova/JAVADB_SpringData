package com.example.java_db_08_lab;

import com.example.java_db_08_lab.model.dto.EmployeeCreateRequest;
import com.example.java_db_08_lab.model.dto.EmployeeCreateResponse;
import com.example.java_db_08_lab.model.dto.ManagerCollection;
import com.example.java_db_08_lab.model.dto.ManagerDto;
import com.example.java_db_08_lab.services.EmployeeService;
import com.example.java_db_08_lab.services.FormatConverter;
import com.example.java_db_08_lab.services.FormatConverterFactory;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class App implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;
    private final BufferedReader reader;
    private final FormatConverterFactory formatConverterFactory;

    //
//
    public App(EmployeeService employeeService, ModelMapper modelMapper, FormatConverterFactory formatConverterFactory) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
        this.formatConverterFactory = formatConverterFactory;
        this.reader = new BufferedReader(new InputStreamReader(System.in));

    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Enter format type: ");
        String formatType = reader.readLine();
        FormatConverter converter = this.formatConverterFactory.create(formatType);
        converter.setPrettyPrint();
        System.out.println("Enter input: ");
        String input = "";
        while (!(input = reader.readLine()).equals("end")) {
            String[] tokens = input.split("\\s+", 2);
            switch (tokens[0]) {
                case "find":
                    long id = Long.parseLong(tokens[1]);
                    ManagerDto managerById = this.employeeService.findManager(id);

                    System.out.println(converter.serialize(managerById));
                    break;
                case "findAll":
                    List<ManagerDto> allManagers = this.employeeService.findAll();

                    System.out.println(converter.serialize(new ManagerCollection(allManagers)));
                    break;
                case "save":
                    String txt = tokens[1];
//                    EmployeeCreateRequest request = this.gson.fromJson(json, EmployeeCreateRequest.class);
//                    EmployeeCreateResponse response = this.employeeService.save(request);
//                    System.out.println(this.gson.toJson(response));

                    //Xml
                    //<?xml version="1.0" encoding="utf-8"?><employee firs_name="From" last_name="Xml"><salary>3012</salary><address>"Mladost 4C"</address></employee>
                    EmployeeCreateRequest request = converter.deserialize(txt, EmployeeCreateRequest.class);
                    EmployeeCreateResponse response = this.employeeService.save(request);
                    System.out.println(converter.serialize(response));
                    break;
                case "save-from-file":
//                  String  request = this.employeeService.save(
//                            this.gson.fromJson(
//                                    new FileReader(
//                                            tokens[1]),
//                                    EmployeeCreateRequest.class
//                            ));

                    EmployeeCreateRequest fileRequest = converter
                            .deserializeFromFile(tokens[1], EmployeeCreateRequest.class);
                    response = this.employeeService.save(fileRequest);
                    System.out.println(converter.serialize(response));

                    break;
                case "findAll-to":
                    List<ManagerDto> managers = this.employeeService.findAll();
                    converter.serialize(new ManagerCollection(managers), tokens[1] + "." + formatType);
                    System.out.println("Written to file " + tokens[1]);
                    break;
            }
        }
    }
}



