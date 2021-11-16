package com.example.java_db_09_exercise_car_dealer_db.controllers;


import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q01.CustomerOrderedRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q02.CarViewRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q03.SuppliersLocalRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q04.CarsWithPartsRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q05.CustomerWithPurchasesRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q06.SaleCarCustomerRootDto;
import com.example.java_db_09_exercise_car_dealer_db.services.*;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.xmlParser.XMLParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.*;

@Component
public class CarDealerController implements CommandLineRunner {


    private final BufferedReader reader;
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;
    private final FileUtil fileUtil;
    private final XMLParser xmlParser;


    public CarDealerController(BufferedReader reader,
                               SupplierService supplierService, PartService partService,
                               CarService carService, CustomerService customerService, SaleService saleService, FileUtil fileUtil, XMLParser xmlParser) {
        this.reader = reader;

        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println("Enter query: ");
        int query = Integer.parseInt(reader.readLine());
        switch (query) {
            case 1 -> Q01OrderedCustomers();
            case 2 -> Q02CarsFromMakeToyota();
            case 3 -> Q03LocalSuppliers();
            case 4 -> Q04CarsWithTheirListOfParts();
            case 5 -> Q05TotalSalesByCustomer();
            case 6 -> Q06SalesWithAppliedDiscount();
        }

    }

    private void Q06SalesWithAppliedDiscount() throws IOException, JAXBException {
        SaleCarCustomerRootDto result = this.saleService
                .findALlSalesWithCustomersAndPrice();
        this.xmlParser.writeToFile(Q06_SALES_WITH_APPLIED_DISCOUNT, result);
    }

    private void Q05TotalSalesByCustomer() throws IOException, JAXBException {
        CustomerWithPurchasesRootDto result = this.customerService.findAllCustomersWithMoreThanOneSale();
        this.xmlParser.writeToFile(Q05_CUSTOMERS_TOTAL_SALES, result);
    }

    private void Q04CarsWithTheirListOfParts() throws JAXBException {
        CarsWithPartsRootDto result = this.carService.findAllCarsWithParts();
        this.xmlParser.writeToFile(Q04_CARS_AND_PARTS, result);
    }

    private void Q03LocalSuppliers() throws JAXBException {
        SuppliersLocalRootDto result = this.supplierService.findAllByImporterIsFalse();
        this.xmlParser.writeToFile(Q03_LOCAL_SUPPLIERS, result);
    }

    private void Q02CarsFromMakeToyota() throws JAXBException {
        String make = "Mercedes";
        CarViewRootDto result = this.carService.findAllByMakeOrderByModelAscTravelledDistanceDesc(make);
        this.xmlParser.writeToFile(Q02_TOYOTA_CARS, result);
    }

    private void Q01OrderedCustomers() throws JAXBException {
        CustomerOrderedRootDto result = this.customerService.findAllByOrderByBirthDateIsYoungDriver();
        this.xmlParser.writeToFile(Q01_ORDERED_CUSTOMERS, result);
    }


    private void seedData() throws IOException, JAXBException {
        this.supplierService.seedSuppliers();
        this.partService.seedParts();
        this.carService.seedCars();
        this.customerService.seedCustomers();
        this.saleService.seedSales();
    }
}
