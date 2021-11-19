package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.DistrictImportDto;
import mostwanted.domain.entities.District;
import mostwanted.domain.entities.Town;
import mostwanted.repository.DistrictRepository;
import mostwanted.repository.TownRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static mostwanted.common.Constants.*;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final static String DISTRICT_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/districts.json";
    private final DistrictRepository districtRepository;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final FileUtil fileUtil;

    public DistrictServiceImpl(DistrictRepository districtRepository, TownRepository townRepository, ValidationUtil validationUtil,
                               ModelMapper modelMapper, Gson gson, FileUtil fileUtil) {
        this.districtRepository = districtRepository;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean districtsAreImported() {
        return this.districtRepository.count() > 0;
    }

    @Override
    public String readDistrictsJsonFile() throws IOException {
        return this.fileUtil.readFile(DISTRICT_JSON_FILE_PATH);
    }

    @Override
    public String importDistricts(String districtsFileContent) {
        StringBuilder sb = new StringBuilder();
        DistrictImportDto[] districtImportDtos = this.gson.fromJson(districtsFileContent, DistrictImportDto[].class);
        for (DistrictImportDto districtImportDto : districtImportDtos) {
            if (!this.validationUtil.isValid(districtImportDto)) {
                getIncorrectDataMessage(sb);
                continue;
            }
            District district = this.modelMapper.map(districtImportDto, District.class);
            Town town = this.townRepository.findByName(districtImportDto.getTown());
            if (town == null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            if (this.districtRepository.findByName(district.getName()) != null) {
                getDuplicateMessage(sb);
                continue;
            }
            district.setTown(town);
            this.districtRepository.saveAndFlush(district);
            getSuccessMessage(sb, District.class.getSimpleName(), district.getName());
        }
        return sb.toString().trim();
    }
}
