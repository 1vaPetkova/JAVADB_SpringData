package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.RacerImportDto;
import mostwanted.domain.entities.Racer;
import mostwanted.domain.entities.Town;
import mostwanted.repository.RacerRepository;
import mostwanted.repository.TownRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static mostwanted.common.Constants.*;

@Service
public class RacerServiceImpl implements RacerService {

    private final static String RACERS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/racers.json";
    private final RacerRepository racerRepository;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final FileUtil fileUtil;

    public RacerServiceImpl(RacerRepository racerRepository, TownRepository townRepository,
                            ValidationUtil validationUtil, ModelMapper modelMapper,
                            Gson gson, FileUtil fileUtil) {
        this.racerRepository = racerRepository;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean racersAreImported() {
        return this.racerRepository.count() > 0;
    }

    @Override
    public String readRacersJsonFile() throws IOException {
        return this.fileUtil.readFile(RACERS_JSON_FILE_PATH);
    }

    @Override
    public String importRacers(String racersFileContent) {
        StringBuilder sb = new StringBuilder();
        RacerImportDto[] racerImportDtos = this.gson.fromJson(racersFileContent, RacerImportDto[].class);
        for (RacerImportDto racerImportDto : racerImportDtos) {
            if (!this.validationUtil.isValid(racerImportDto)) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Racer racer = this.modelMapper.map(racerImportDto, Racer.class);
            Town town = this.townRepository.findByName(racerImportDto.getHomeTown());
            if (town == null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            if (this.racerRepository.findByName(racer.getName()) != null) {
                getDuplicateMessage(sb);
                continue;
            }
            racer.setHomeTown(town);
            this.racerRepository.saveAndFlush(racer);
            getSuccessMessage(sb, Racer.class.getSimpleName(), racer.getName());
        }

        return sb.toString().trim();
    }

    @Override
    public String exportRacingCars() {
        StringBuilder sb = new StringBuilder();
        this.racerRepository
                .findRacersWithCars()
                .forEach(r -> {
                            sb
                                    .append(String.format("Name: %s%n", r.getName()))
                                    .append(r.getAge() != null
                                            ? String.format("Age: %d%n", r.getAge())
                                            : "")
                                    .append("Cars:")
                                    .append(System.lineSeparator());
                            r.getCars()
                                    .forEach(c -> sb
                                            .append(String.format("%s %s %d",
                                                    c.getBrand(), c.getModel(), c.getYearOfProduction()))
                                            .append(System.lineSeparator()));
                            sb
                                    .append(System.lineSeparator());
                        }
                );
        return sb.toString().trim();
    }
}
