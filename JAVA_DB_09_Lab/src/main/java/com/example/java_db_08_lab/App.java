package com.example.java_db_08_lab;

import com.example.java_db_08_lab.model.dto.EmployeeCreateRequest;
import com.example.java_db_08_lab.model.dto.EmployeeCreateResponse;
import com.example.java_db_08_lab.model.dto.ManagerDto;
import com.example.java_db_08_lab.services.EmployeeService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class App implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;
    private final BufferedReader reader;
    private final Gson gson;


    public App(EmployeeService employeeService, ModelMapper modelMapper, Gson gson) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Enter input: ");
        String input = "";
        while (!(input = reader.readLine()).equals("end")) {
            String[] tokens = input.split("\\s+", 2);
            switch (tokens[0]) {
                case "find":
                    long id = Long.parseLong(tokens[1]);
                    ManagerDto managerById = this.employeeService.findManager(id);
                    System.out.println(this.gson.toJson(managerById));
                    break;
                case "findAll":
                    List<ManagerDto> allManagers = this.employeeService.findAll();
                    System.out.println(this.gson.toJson(allManagers));
                    break;
                case "save":
                    String json = tokens[1];
                    EmployeeCreateRequest request = this.gson.fromJson(json, EmployeeCreateRequest.class);
                    EmployeeCreateResponse response = this.employeeService.save(request);
                    System.out.println(this.gson.toJson(response));
                    break;
                case "save-from-file":
                    request = this.employeeService.save(
                            this.gson.fromJson(
                                    new FileReader(
                                            tokens[1]),
                                    EmployeeCreateRequest.class
                            ));
                    response = this.employeeService.save(request);
                    System.out.println(this.gson.toJson(response));
                    break;
                case "findAll-to":
                    FileWriter fileWriter = new FileWriter(tokens[1]);
                    this.gson.toJson(
                            this.employeeService.findAll(),fileWriter

                    );
                    fileWriter.flush();
                    fileWriter.close();
                    System.out.println("Written to file " + tokens[1]);
                    break;
            }


        }
    }

}

