package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.TownImportDto;
import mostwanted.domain.entities.Town;
import mostwanted.repository.TownRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static mostwanted.common.Constants.*;

@Service
public class TownServiceImpl implements TownService {


    private final static String TOWNS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/towns.json";
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final FileUtil fileUtil;

    public TownServiceImpl(TownRepository townRepository,
                           ValidationUtil validationUtil, ModelMapper modelMapper,
                           Gson gson, FileUtil fileUtil) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.fileUtil.readFile(TOWNS_JSON_FILE_PATH);
    }

    @Override
    public String importTowns(String townsFileContent) {
        StringBuilder sb = new StringBuilder();
        TownImportDto[] townImportDtos = this.gson.fromJson(townsFileContent, TownImportDto[].class);
        for (TownImportDto townImportDto : townImportDtos) {
            if (!this.validationUtil.isValid(townImportDto)) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Town town = this.modelMapper.map(townImportDto, Town.class);
            if (this.townRepository.findByName(town.getName()) != null) {
                getDuplicateMessage(sb);
                continue;
            }
            this.townRepository.saveAndFlush(town);
            getSuccessMessage(sb, Town.class.getSimpleName(), town.getName());
        }

        return sb.toString().trim();
    }


    @Override
    public String exportRacingTowns() {
        StringBuilder sb = new StringBuilder();
        this.townRepository.findAllTownsWithRacers()
                .forEach(t -> sb
                        .append(String.format("Name: %s", t.getName()))
                        .append(System.lineSeparator())
                        .append(String.format("Racers: %d", t.getRacers().size()))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                );
        return sb.toString().trim();
    }


}
