package exam.service;

import exam.model.dto.seed.xml.TownSeedDto;
import exam.model.dto.seed.xml.TownSeedRootDto;
import exam.model.entities.Town;
import exam.repository.TownRepository;
import exam.util.FileUtil;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static exam.common.Constants.*;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, ValidationUtil validationUtil,
                           FileUtil fileUtil, XmlParser xmlParser, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
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
    public String importTowns() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        TownSeedRootDto townSeedRootDto = this.xmlParser.parseXml(TownSeedRootDto.class, TOWNS_PATH);
        for (TownSeedDto townSeedDto : townSeedRootDto.getTowns()) {
            if (!this.validationUtil.isValid(townSeedDto)
                    || this.townRepository.findByName(townSeedDto.getName()) != null) {
                getIncorrectDataMessage(sb, "town");
                continue;
            }
            Town town = this.modelMapper.map(townSeedDto, Town.class);
            this.townRepository.save(town);
            getSuccessTownMessage(sb, town.getName());
        }

        return sb.toString().trim();
    }
}
