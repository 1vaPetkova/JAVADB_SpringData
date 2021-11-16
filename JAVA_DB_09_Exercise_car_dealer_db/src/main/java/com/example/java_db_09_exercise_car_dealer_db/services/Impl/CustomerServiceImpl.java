package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.CustomerSeedDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CarViewDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CustomerWithPurchasesDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.CustomersOrderedDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.SaleViewDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Customer;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Sale;
import com.example.java_db_09_exercise_car_dealer_db.repositories.CustomerRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.CustomerService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.CUSTOMERS_PATH;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, Gson gson,
                               FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedCustomers() throws IOException {
        if (this.customerRepository.count() == 0) {
            Arrays
                    .stream(gson.fromJson(fileUtil.readFileContent(CUSTOMERS_PATH), CustomerSeedDto[].class))
                    .filter(validationUtil::isValid)
                    .map(customerSeedDto -> modelMapper.map(customerSeedDto, Customer.class))
                    .forEach(this.customerRepository::save);
        }
    }

    @Override
    public Customer findRandomCustomer() {
        long randomId = ThreadLocalRandom
                .current()
                .nextLong(1, this.customerRepository.count() + 1);
        return this.customerRepository.findById(randomId).orElse(null);
    }

    //Q01
    @Override
    public List<CustomersOrderedDto> findAllByOrderByBirthDateIsYoungDriver() {
        List<Customer> customers = this.customerRepository.findAllByOrderByBirthDateAscIsYoungDriverAsc();
        return customers
                .stream()
                .map(customer -> {
                    CustomersOrderedDto customersOrderedDto = modelMapper.map(customer, CustomersOrderedDto.class);
                    Set<Sale> sales = customer.getSales();
                    Set<SaleViewDto> saleViewDtos = new HashSet<>();
                    for (Sale sale : sales) {
                        SaleViewDto saleViewDto = modelMapper.map(sale, SaleViewDto.class);
                        CarViewDto carViewDto = modelMapper.map(sale.getCar(), CarViewDto.class);
                        saleViewDto.setCar(carViewDto);
                        saleViewDtos.add(saleViewDto);
                    }
                    customersOrderedDto.setSales(saleViewDtos);
                    return customersOrderedDto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<CustomerWithPurchasesDto> findAllCustomersWithMoreThanOneSale() {
        return this.customerRepository
                .findAllCustomersWithMoreThanOneSale()
                .stream()
                .map(customer -> {
                            CustomerWithPurchasesDto customerWithPurchasesDto = modelMapper
                                    .map(customer, CustomerWithPurchasesDto.class);
                            customerWithPurchasesDto.setBoughtCars(customer.getSales().size());
                            Set<Sale> sales = customer.getSales();
                            BigDecimal spentMoney = new BigDecimal("0");
                            for (Sale sale : sales) {
                                List<BigDecimal> prices = sale.getCar().getParts()
                                        .stream()
                                        .map(Part::getPrice)
                                        .collect(Collectors.toList());
                                for (BigDecimal price : prices) {
                                    spentMoney = spentMoney.add(price);
                                }
                            }
                            customerWithPurchasesDto.setSpentMoney(spentMoney);
                            return customerWithPurchasesDto;
                        }
                ).collect(Collectors.toList())
                .stream().sorted((c1, c2) -> {
                    int result = Double.compare(c2.getSpentMoney().doubleValue(), c1.getSpentMoney().doubleValue());
                    if (result == 0) {
                        result = Integer.compare(c2.getBoughtCars(), c1.getBoughtCars());
                    }
                    return result;
                }
        )
                .collect(Collectors.toList());
    }

}
