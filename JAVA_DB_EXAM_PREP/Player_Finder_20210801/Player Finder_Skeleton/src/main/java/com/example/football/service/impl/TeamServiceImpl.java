package com.example.football.service.impl;

import com.example.football.models.dto.seed.json.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.FileUtil;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.football.common.Constants.*;


@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TownRepository townRepository, ValidationUtil validationUtil,
                           FileUtil fileUtil, Gson gson, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;

        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return this.fileUtil.readFile(TEAMS_PATH);
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        TeamSeedDto[] teamSeedDtos = this.gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class);

        for (TeamSeedDto teamSeedDto : teamSeedDtos) {
            if (this.teamRepository.existsByName(teamSeedDto.getName())
                    || !this.validationUtil.isValid(teamSeedDto)) {
                getIncorrectDataMessage(sb, "Team");
                continue;
            }
            Town town = this.townRepository.findByName(teamSeedDto.getName());
            Team team = this.modelMapper.map(teamSeedDto, Team.class);
            team.setTown(town);
            this.teamRepository.save(team);
            getSuccessTeamMessage(sb, team.getName(), team.getFanBase());
        }
        return sb.toString().trim();
    }
}
