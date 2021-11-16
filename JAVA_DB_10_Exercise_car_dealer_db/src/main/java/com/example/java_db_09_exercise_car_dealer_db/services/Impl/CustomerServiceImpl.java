package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.CustomerSeedRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q05.CustomerWithPurchasesDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q01.CustomerOrderedRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q01.CustomersOrderedDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q05.CustomerWithPurchasesRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Customer;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Sale;
import com.example.java_db_09_exercise_car_dealer_db.repositories.CustomerRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.CustomerService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.CUSTOMERS_PATH;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper, XMLParser xmlParser) {
        this.customerRepository = customerRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }


    @Override
    public void seedCustomers() throws IOException, JAXBException {
        if (this.customerRepository.count() == 0) {
            CustomerSeedRootDto customerSeedRootDto = this.xmlParser.fromFile(CUSTOMERS_PATH, CustomerSeedRootDto.class);
            customerSeedRootDto.getCustomers()
                    .stream()
                    .filter(this.validationUtil::isValid)
                    .map(customerSeedDto -> this.modelMapper.map(customerSeedDto, Customer.class))
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
    public CustomerOrderedRootDto findAllByOrderByBirthDateIsYoungDriver() {
        CustomerOrderedRootDto customerOrderedRootDto = new CustomerOrderedRootDto();
        List<Customer> customers = this.customerRepository.findAllByOrderByBirthDateAscIsYoungDriverAsc();
        customerOrderedRootDto.setCustomers(customers
                .stream()
                .map(customer -> this.modelMapper.map(customer, CustomersOrderedDto.class))
                .collect(Collectors.toList()));
        return customerOrderedRootDto;

    }

    @Override
    public CustomerWithPurchasesRootDto findAllCustomersWithMoreThanOneSale() {
        CustomerWithPurchasesRootDto customerWithPurchasesRootDto = new CustomerWithPurchasesRootDto();
        customerWithPurchasesRootDto.setCustomers(this.customerRepository
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
                .collect(Collectors.toList()));
        return customerWithPurchasesRootDto;
    }

}
