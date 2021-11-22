package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.seed.json.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static softuni.exam.common.Constants.*;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PictureRepository pictureRepository;
    private final TeamRepository teamRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final Gson gson;

    public PlayerServiceImpl(PlayerRepository playerRepository, PictureRepository pictureRepository,
                             TeamRepository teamRepository, ValidationUtil validationUtil,
                             ModelMapper modelMapper, FileUtil fileUtil, Gson gson) {
        this.playerRepository = playerRepository;
        this.pictureRepository = pictureRepository;
        this.teamRepository = teamRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.gson = gson;
    }

    @Override
    public String importPlayers() throws IOException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedDto[] playerSeedDtos = this.gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class);
        for (PlayerSeedDto playerSeedDto : playerSeedDtos) {
            String teamName = playerSeedDto.getTeam().getName();
            String url = playerSeedDto.getTeam().getPicture().getUrl();
            Picture picture = this.pictureRepository.findByUrl(url);
            Team team = this.teamRepository.findByNameAndPicture_Url(teamName, url);
            if (!this.validationUtil.isValid(playerSeedDto)
                    || picture == null
                    || team == null) {
                getIncorrectDataMessage(sb, "player");
                continue;
            }
            Player player = this.modelMapper.map(playerSeedDto, Player.class);
            player.setPicture(picture);
            player.setTeam(team);
            this.playerRepository.save(player);
            getSuccessPlayerMessage(sb, player.getFirstName(), player.getLastName());
        }
        return sb.toString().trim();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return this.fileUtil.readFile(PLAYERS_PATH);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();
        List<Player> players = this.playerRepository
                .findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000));
        for (Player player : players) {
            sb
                    .append(String.format("Player name: %s %s \n" +
                            "Number: %d\n" +
                            "Salary: %.2f\n" +
                            "Team: %s\n",
                            player.getFirstName(),
                            player.getLastName(),
                            player.getNumber(),
                            player.getSalary(),
                            player.getTeam().getName()));
        }
        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        String teamName = "North Hub";
        StringBuilder sb = new StringBuilder();
        List<Player> players = this.playerRepository.findAllPlayersInGivenTeam(teamName);
        sb.append("Team: ").append(teamName).append(System.lineSeparator());
        for (Player player : players) {
            sb
                    .append(String.format("Player name: %s %s - %s\n",
                            player.getFirstName(), player.getLastName(), player.getPosition().name()))
                    .append(String.format("Number: %d\n", player.getNumber()));
        }
        return sb.toString().trim();
    }
}
