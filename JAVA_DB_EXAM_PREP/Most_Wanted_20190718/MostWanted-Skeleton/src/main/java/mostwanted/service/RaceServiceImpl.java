package mostwanted.service;

import mostwanted.domain.dtos.races.EntryIdDto;
import mostwanted.domain.dtos.races.RaceImportDto;
import mostwanted.domain.dtos.races.RaceImportRootDto;
import mostwanted.domain.entities.District;
import mostwanted.domain.entities.Race;
import mostwanted.domain.entities.RaceEntry;
import mostwanted.repository.DistrictRepository;
import mostwanted.repository.RaceEntryRepository;
import mostwanted.repository.RaceRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import mostwanted.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static mostwanted.common.Constants.*;

@Service
public class RaceServiceImpl implements RaceService {

    private final static String RACES_XML_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/races.xml";
    private final RaceRepository raceRepository;
    private final RaceEntryRepository raceEntryRepository;
    private final DistrictRepository districtRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final FileUtil fileUtil;

    public RaceServiceImpl(RaceRepository raceRepository, RaceEntryRepository raceEntryRepository,
                           DistrictRepository districtRepository, ValidationUtil validationUtil,
                           ModelMapper modelMapper,
                           XmlParser xmlParser, FileUtil fileUtil) {
        this.raceRepository = raceRepository;
        this.raceEntryRepository = raceEntryRepository;
        this.districtRepository = districtRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean racesAreImported() {
        return this.raceRepository.count() > 0;
    }

    @Override
    public String readRacesXmlFile() throws IOException {
        return this.fileUtil.readFile(RACES_XML_FILE_PATH);
    }

    @Override
    public String importRaces() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        RaceImportRootDto raceImportRootDto = this.xmlParser.parseXml(RaceImportRootDto.class, RACES_XML_FILE_PATH);

        for (RaceImportDto raceImportDto : raceImportRootDto.getRaces()) {
            if (!this.validationUtil.isValid(raceImportDto)) {
                getIncorrectDataMessage(sb);
                continue;
            }
            District district = this.districtRepository.findByName(raceImportDto.getDistrictName());
            if (district == null) {
                getIncorrectDataMessage(sb);
            }
            Set<EntryIdDto> entries = raceImportDto.getEntries();
            Set<RaceEntry> raceEntries = new HashSet<>();
            for (EntryIdDto entry : entries) {
                RaceEntry raceEntry = this.raceEntryRepository.findById(entry.getId()).orElse(null);
                if (raceEntry == null) {
                    getIncorrectDataMessage(sb);
                    continue;
                }
                raceEntries.add(raceEntry);
            }
            Race race = this.modelMapper.map(raceImportDto, Race.class);
            race.setDistrict(district);
            race.setEntries(raceEntries);
            if (this.raceRepository.findByLapsAndDistrict(race.getLaps(), district) != null) {
                getDuplicateMessage(sb);
                continue;
            }
            this.raceRepository.saveAndFlush(race);
            getSuccessMessage(sb, Race.class.getSimpleName(), String.valueOf(race.getId()));
        }

        return sb.toString().trim();
    }
}