package mostwanted.service;

import mostwanted.domain.dtos.races.EntryImportDto;
import mostwanted.domain.dtos.races.EntryImportRootDto;
import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.RaceEntry;
import mostwanted.domain.entities.Racer;
import mostwanted.repository.CarRepository;
import mostwanted.repository.RaceEntryRepository;
import mostwanted.repository.RacerRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import mostwanted.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static mostwanted.common.Constants.getIncorrectDataMessage;
import static mostwanted.common.Constants.getSuccessMessage;

@Service
public class RaceEntryServiceImpl implements RaceEntryService {

    private final static String RACE_ENTRIES_XML_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/race-entries.xml";
    private final RaceEntryRepository raceEntryRepository;
    private final RacerRepository racerRepository;
    private final CarRepository carRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final FileUtil fileUtil;

    public RaceEntryServiceImpl(RaceEntryRepository raceEntryRepository, RacerRepository racerRepository,
                                CarRepository carRepository, ValidationUtil validationUtil,
                                ModelMapper modelMapper, XmlParser xmlParser,
                                FileUtil fileUtil) {
        this.raceEntryRepository = raceEntryRepository;
        this.racerRepository = racerRepository;
        this.carRepository = carRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean raceEntriesAreImported() {
        return this.raceEntryRepository.count() > 0;
    }

    @Override
    public String readRaceEntriesXmlFile() throws IOException {
        return this.fileUtil.readFile(RACE_ENTRIES_XML_FILE_PATH);
    }

    @Override
    public String importRaceEntries() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        EntryImportRootDto entryImportRootDto = this.xmlParser.parseXml(EntryImportRootDto.class, RACE_ENTRIES_XML_FILE_PATH);
        for (EntryImportDto entryImportDto : entryImportRootDto.getRaceEntries()) {
            if (!this.validationUtil.isValid(entryImportDto)) {
                getIncorrectDataMessage(sb);
                continue;
            }

            Car car = this.carRepository.findById(entryImportDto.getCar()).orElse(null);
            if (car == null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Racer racer = this.racerRepository.findByName(entryImportDto.getRacer());
            if (racer == null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            RaceEntry raceEntry = this.modelMapper.map(entryImportDto, RaceEntry.class);
            raceEntry.setRacer(racer);
            raceEntry.setCar(car);
            this.raceEntryRepository.saveAndFlush(raceEntry);
            getSuccessMessage(sb, RaceEntry.class.getSimpleName(), String.valueOf(raceEntry.getId()));
        }
        return sb.toString().trim();
    }
}