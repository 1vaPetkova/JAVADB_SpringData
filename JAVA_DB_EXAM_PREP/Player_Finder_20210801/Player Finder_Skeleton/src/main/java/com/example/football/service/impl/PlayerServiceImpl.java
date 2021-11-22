package com.example.football.service.impl;

import com.example.football.models.dto.seed.xml.PlayerSeedDto;
import com.example.football.models.dto.seed.xml.PlayerSeedRootDto;
import com.example.football.models.dto.view.PlayerViewDto;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.FileUtil;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static com.example.football.common.Constants.*;


@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final TeamRepository teamRepository;
    private final StatRepository statRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, TownRepository townRepository,
                             TeamRepository teamRepository, StatRepository statRepository,
                             ValidationUtil validationUtil, FileUtil fileUtil,
                             XmlParser xmlParser, ModelMapper modelMapper) {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return this.fileUtil.readFile(PLAYERS_PATH);
    }

    @Override
    public String importPlayers() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        PlayerSeedRootDto playerSeedRootDto = this.xmlParser.parseXml(PlayerSeedRootDto.class, PLAYERS_PATH);
        for (PlayerSeedDto playerSeedDto : playerSeedRootDto.getPlayers()) {
            if (this.playerRepository.existsByEmail(playerSeedDto.getEmail())
                    || !this.validationUtil.isValid(playerSeedDto)) {
                getIncorrectDataMessage(sb, "Player");
                continue;
            }
            Town town = this.townRepository.findByName(playerSeedDto.getTown().getName());
            Team team = this.teamRepository.findByName(playerSeedDto.getTeam().getName());
            Stat stat = this.statRepository.findById(playerSeedDto.getStat().getId()).orElse(null);

            Player player = this.modelMapper.map(playerSeedDto, Player.class);
            player.setTown(town);
            player.setTeam(team);
            player.setStat(stat);

            this.playerRepository.save(player);
            getSuccessPlayerMessage(sb, player.getFirstName(), player.getLastName(), player.getPosition().name());

        }

        return sb.toString().trim();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();
        List<Player> bestPlayers = this.playerRepository.findBestPlayers();
        for (Player bestPlayer : bestPlayers) {
            PlayerViewDto playerView = this.modelMapper.map(bestPlayer, PlayerViewDto.class);
            playerView.setStadiumName(bestPlayer.getTeam().getStadiumName());
            playerView.setTeamName(bestPlayer.getTeam().getName());
            sb.append(playerView).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
