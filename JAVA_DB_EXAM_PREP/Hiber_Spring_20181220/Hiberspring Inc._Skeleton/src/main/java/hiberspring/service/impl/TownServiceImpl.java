package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.seed.json.TownSeedDto;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public TownServiceImpl(TownRepository townRepository, FileUtil fileUtil,
                           ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.townRepository = townRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.fileUtil.readFile(TOWNS_PATH);
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        TownSeedDto[] townSeedDtos = this.gson.fromJson(readTownsJsonFile(), TownSeedDto[].class);
        for (TownSeedDto townSeedDto : townSeedDtos) {
            if (!this.validationUtil.isValid(townSeedDto)) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Town town = this.modelMapper.map(townSeedDto, Town.class);
            this.townRepository.save(town);
            getSuccessTownMessage(sb,town.getName());
        }
        return sb.toString().trim();
    }
}
