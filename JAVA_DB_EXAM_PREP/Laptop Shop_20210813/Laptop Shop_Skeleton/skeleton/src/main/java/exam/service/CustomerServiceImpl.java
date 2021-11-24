package exam.service;

import com.google.gson.Gson;
import exam.model.dto.seed.json.CustomerSeedDto;
import exam.model.entities.Customer;
import exam.model.entities.Town;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.util.FileUtil;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static exam.common.Constants.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, TownRepository townRepository,
                               ValidationUtil validationUtil, FileUtil fileUtil, Gson gson, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return this.fileUtil.readFile(CUSTOMERS_PATH);
    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder sb = new StringBuilder();
        CustomerSeedDto[] customerSeedDtos = this.gson.fromJson(readCustomersFileContent(), CustomerSeedDto[].class);
        for (CustomerSeedDto customerSeedDto : customerSeedDtos) {
            if (!this.validationUtil.isValid(customerSeedDto)
                    || this.customerRepository.findByEmail(customerSeedDto.getEmail()) != null){
                getIncorrectDataMessage(sb,"customer");
                continue;
            }
            Customer customer = this.modelMapper.map(customerSeedDto, Customer.class);
            Town town = this.townRepository.findByName(customerSeedDto.getTown().getName());
            customer.setTown(town);
            this.customerRepository.save(customer);
            getSuccessCustomerMessage(sb,customer.getFirstName(),customer.getLastName(), customer.getEmail());
        }
        return sb.toString().trim();
    }
}
