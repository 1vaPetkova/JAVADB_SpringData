package hiberspring.service.impl;

import hiberspring.domain.dtos.seed.xml.EmployeeSeedDto;
import hiberspring.domain.dtos.seed.xml.EmployeeSeedRootDto;
import hiberspring.domain.dtos.view.EmployeeViewDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.EmployeeService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final EmployeeCardRepository employeeCardRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BranchRepository branchRepository,
                               EmployeeCardRepository employeeCardRepository, FileUtil fileUtil,
                               ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.employeeCardRepository = employeeCardRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEES_PATH);
    }

    @Override
    public String importEmployees() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        EmployeeSeedRootDto employeeSeedRootDto = this.xmlParser.parseXml(EmployeeSeedRootDto.class, EMPLOYEES_PATH);
        for (EmployeeSeedDto employeeSeedDto : employeeSeedRootDto.getEmployees()) {
            Branch branch = this.branchRepository.findByName(employeeSeedDto.getBranch());
            EmployeeCard employeeCard = this.employeeCardRepository.findByNumber(employeeSeedDto.getCard());

            if (!this.validationUtil.isValid(employeeSeedDto)
                    || branch == null
                    || this.employeeRepository.findByCard(employeeCard) != null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Employee employee = this.modelMapper.map(employeeSeedDto, Employee.class);
            employee.setBranch(branch);
            employee.setCard(employeeCard);
            this.employeeRepository.save(employee);
            getSuccessEmployeeMessage(sb, employee.getFirstName(), employee.getLastName());
        }
        return sb.toString().trim();
    }

    @Override
    public String exportProductiveEmployees() {
        StringBuilder sb = new StringBuilder();
        List<EmployeeViewDto> result =
                this.employeeRepository.findAllByBranchWithProducts();
        for (EmployeeViewDto employee : result) {
            sb
                    .append(String.format("Name: %s\n",employee.getFullName()))
                    .append(String.format("Position: %s\n",employee.getPosition()))
                    .append(String.format("Card Number: %s\n",employee.getCardNumber()))
                    .append("-------------------------\n");
        }

        return sb.toString().trim();
    }
}
