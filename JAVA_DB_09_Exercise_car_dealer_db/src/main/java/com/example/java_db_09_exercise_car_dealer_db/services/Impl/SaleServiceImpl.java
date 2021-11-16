package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CarSaleDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.SaleCarCustomerDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Car;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Customer;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Sale;
import com.example.java_db_09_exercise_car_dealer_db.repositories.SaleRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.CarService;
import com.example.java_db_09_exercise_car_dealer_db.services.CustomerService;
import com.example.java_db_09_exercise_car_dealer_db.services.SaleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CarService carService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public SaleServiceImpl(SaleRepository saleRepository, CarService carService, CustomerService customerService, ModelMapper modelMapper) {
        this.saleRepository = saleRepository;
        this.carService = carService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedSales() {
        int randomSalesCount = ThreadLocalRandom
                .current()
                .nextInt(1, 10);
        for (int i = 0; i < randomSalesCount; i++) {
            Sale sale = new Sale();
            sale.setCar(this.carService.findRandomCar());
            Customer randomCustomer = this.customerService.findRandomCustomer();
            sale.setCustomer(randomCustomer);
            sale.setDiscount(findRandomDiscount());
            if (randomCustomer.getYoungDriver()) {
                sale.setDiscount(sale.getDiscount() + 0.05);
            }
            this.saleRepository.save(sale);
        }

    }

    //Q06
    @Override
    public List<SaleCarCustomerDto> findALlSalesWithCustomersAndPrice() {
        return this.saleRepository
                .findAll()
                .stream()
                .map(sale -> {
                    //mapped discount, customer name
                    SaleCarCustomerDto saleDto = modelMapper.map(sale, SaleCarCustomerDto.class);
                    Car car = sale.getCar();
                    //mapped car
                    saleDto.setCar(modelMapper.map(car, CarSaleDto.class));

                    //mapped price
                    BigDecimal price = new BigDecimal("0");
                    for (Part part : car.getParts()) {
                        price = price.add(part.getPrice());
                    }
                    saleDto.setPrice(price);
                    saleDto.setPriceWithDiscount(price.multiply(BigDecimal.valueOf(1 - saleDto.getDiscount())));

                    return saleDto;
                })
                .collect(Collectors.toList());

    }

    private Double findRandomDiscount() {
        List<Integer> discounts = Arrays.asList(0, 5, 10, 15, 20, 30, 40, 50);
        int randomIndex = ThreadLocalRandom
                .current()
                .nextInt(0, discounts.size());
        return discounts.get(randomIndex) / 100.0;
    }
}
