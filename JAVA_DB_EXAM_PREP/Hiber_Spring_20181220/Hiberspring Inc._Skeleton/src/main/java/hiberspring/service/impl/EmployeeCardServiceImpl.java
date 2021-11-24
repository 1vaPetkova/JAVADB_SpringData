package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.seed.json.EmployeeCardSeedDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {

    private final EmployeeCardRepository employeeCardRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository,
                                   FileUtil fileUtil, ModelMapper modelMapper,
                                   ValidationUtil validationUtil, Gson gson) {
        this.employeeCardRepository = employeeCardRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEE_CARDS_PATH);
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        EmployeeCardSeedDto[] employeeCardSeedDtos = this.gson.fromJson(readEmployeeCardsJsonFile(), EmployeeCardSeedDto[].class);
        for (EmployeeCardSeedDto employeeCardSeedDto : employeeCardSeedDtos) {
            if (!this.validationUtil.isValid(employeeCardSeedDto) ||
                    this.employeeCardRepository.findByNumber(employeeCardSeedDto.getNumber()) != null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            EmployeeCard employeeCard = this.modelMapper.map(employeeCardSeedDto, EmployeeCard.class);
            this.employeeCardRepository.save(employeeCard);
            getSuccessEmployeeCardMessage(sb, employeeCard.getNumber());
        }

        return sb.toString().trim();
    }
}
