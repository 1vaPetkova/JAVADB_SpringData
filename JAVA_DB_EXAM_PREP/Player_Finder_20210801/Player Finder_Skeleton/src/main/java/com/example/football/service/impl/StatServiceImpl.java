package com.example.football.service.impl;

import com.example.football.models.dto.seed.xml.StatSeedDto;
import com.example.football.models.dto.seed.xml.StatSeedRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.FileUtil;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static com.example.football.common.Constants.*;


@Service
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    public StatServiceImpl(StatRepository statRepository, ValidationUtil validationUtil,
                           FileUtil fileUtil, XmlParser xmlParser, ModelMapper modelMapper) {
        this.statRepository = statRepository;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return this.fileUtil.readFile(STATS_PATH);
    }

    @Override
    public String importStats() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        StatSeedRootDto statSeedRootDto = this.xmlParser.parseXml(StatSeedRootDto.class, STATS_PATH);
        for (StatSeedDto statSeedDto : statSeedRootDto.getStats()) {
            if (!this.validationUtil.isValid(statSeedDto)) {
                getIncorrectDataMessage(sb, "Stat");
                continue;
            }

            Stat stat = this.modelMapper.map(statSeedDto, Stat.class);

            this.statRepository.save(stat);
            getSuccessStatMessage(sb, stat.getPassing(), stat.getShooting(), stat.getEndurance());
        }
        return sb.toString().trim();
    }
}
