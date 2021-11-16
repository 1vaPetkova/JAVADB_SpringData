package com.example.java_db_09_exercise_car_dealer_db.controllers;


import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.*;
import com.example.java_db_09_exercise_car_dealer_db.services.*;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.*;

@Component
public class CarDealerController implements CommandLineRunner {


    private final BufferedReader reader;
    private final Gson gson;
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;
    private final FileUtil fileUtil;

    public CarDealerController(BufferedReader reader, Gson gson,
                               SupplierService supplierService, PartService partService,
                               CarService carService, CustomerService customerService, SaleService saleService, FileUtil fileUtil) {
        this.reader = reader;
        this.gson = gson;
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
        this.fileUtil = fileUtil;
    }


    @Override
    public void run(String... args) throws Exception {
     //   seedData();
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

    private void Q06SalesWithAppliedDiscount() throws IOException {
        List<SaleCarCustomerDto> aLlSalesWithCustomersAndPrice = this.saleService
                .findALlSalesWithCustomersAndPrice();
        fileUtil.writeToFile(Q06_SALES_WITH_APPLIED_DISCOUNT,gson.toJson(aLlSalesWithCustomersAndPrice));
    }

    private void Q05TotalSalesByCustomer() throws IOException {
        List<CustomerWithPurchasesDto> allCustomersWithMoreThanOneSale = this.customerService
                .findAllCustomersWithMoreThanOneSale();
        fileUtil.writeToFile(Q05_CUSTOMERS_TOTAL_SALES, gson.toJson(allCustomersWithMoreThanOneSale));
    }

    private void Q04CarsWithTheirListOfParts() throws IOException {
        List<CarsWithPartsDto> allCars = this.carService.findAllCarsWithParts();
        fileUtil.writeToFile(Q04_CARS_AND_PARTS, gson.toJson(allCars));
    }

    private void Q03LocalSuppliers() throws IOException {
        List<SuppliersLocalDto> allByImporterIsFalse = this.supplierService
                .findAllByImporterIsFalse();
        fileUtil.writeToFile(Q03_LOCAL_SUPPLIERS, gson.toJson(allByImporterIsFalse));
    }

    private void Q02CarsFromMakeToyota() throws IOException {
        String make = "Toyota";
        List<CarViewDto> allByMakeOrderByModelTravelledDistanceDesc = this.carService
                .findAllByMakeOrderByModelAscTravelledDistanceDesc(make);
        fileUtil.writeToFile(Q02_TOYOTA_CARS, gson.toJson(allByMakeOrderByModelTravelledDistanceDesc));
    }

    private void Q01OrderedCustomers() throws IOException {
        List<CustomersOrderedDto> allOrderByBirthDateAndIsYoungDriver = this.customerService
                .findAllByOrderByBirthDateIsYoungDriver();
        fileUtil.writeToFile(Q01_ORDERED_CUSTOMERS, gson.toJson(allOrderByBirthDateAndIsYoungDriver));
    }


    private void seedData() throws IOException {
        this.supplierService.seedSuppliers();
        this.partService.seedParts();
        this.carService.seedCars();
        this.customerService.seedCustomers();
        this.saleService.seedSales();
    }
}
