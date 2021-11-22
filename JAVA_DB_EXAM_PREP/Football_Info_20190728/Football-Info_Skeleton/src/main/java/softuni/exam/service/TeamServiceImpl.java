package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.seed.xml.TeamSeedDto;
import softuni.exam.domain.dto.seed.xml.TeamSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static softuni.exam.common.Constants.*;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;

    public TeamServiceImpl(TeamRepository teamRepository, PictureRepository pictureRepository,
                           ValidationUtil validationUtil, ModelMapper modelMapper,
                           FileUtil fileUtil, XmlParser xmlParser) {
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public String importTeams() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        TeamSeedRootDto teamSeedRootDto = this.xmlParser.parseXml(TeamSeedRootDto.class, TEAMS_PATH);
        for (TeamSeedDto teamSeedDto : teamSeedRootDto.getTeams()) {
            if (!validationUtil.isValid(teamSeedDto)
                    || !this.pictureRepository.existsByUrl(teamSeedDto.getPicture().getUrl())) {
                getIncorrectDataMessage(sb, "team");
                continue;
            }
            Picture picture = this.pictureRepository.findByUrl(teamSeedDto.getPicture().getUrl());
            Team team = this.modelMapper.map(teamSeedDto, Team.class);
            team.setPicture(picture);
            this.teamRepository.save(team);
            getSuccessTeamMessage(sb, team.getName());
        }

        return sb.toString().trim();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return this.fileUtil.readFile(TEAMS_PATH);
    }
}
