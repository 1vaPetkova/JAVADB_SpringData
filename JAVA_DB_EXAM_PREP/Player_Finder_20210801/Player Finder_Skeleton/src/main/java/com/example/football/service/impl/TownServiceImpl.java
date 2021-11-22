package com.example.football.service.impl;

import com.example.football.models.dto.seed.json.TownSeedDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.FileUtil;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.football.common.Constants.*;



@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, ValidationUtil validationUtil,
                           FileUtil fileUtil, Gson gson, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return this.fileUtil.readFile(TOWNS_PATH);
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        TownSeedDto[] townSeedDtos = this.gson.fromJson(readTownsFileContent(), TownSeedDto[].class);
        for (TownSeedDto townSeedDto : townSeedDtos) {
            if (this.townRepository.existsByName(townSeedDto.getName())
                    || !this.validationUtil.isValid(townSeedDto)) {
                getIncorrectDataMessage(sb, "Town");
                continue;
            }
            Town town = this.modelMapper.map(townSeedDto, Town.class);
            this.townRepository.save(town);
            getSuccessTownMessage(sb, town.getName(), town.getPopulation());
        }

        return sb.toString().trim();
    }

}
