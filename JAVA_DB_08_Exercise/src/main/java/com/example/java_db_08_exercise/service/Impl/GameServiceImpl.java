package com.example.java_db_08_exercise.service.Impl;

import com.example.java_db_08_exercise.model.dto.GameAddDto;
import com.example.java_db_08_exercise.model.dto.GameDetailsDto;
import com.example.java_db_08_exercise.model.dto.GamesTitleAndPriceDto;
import com.example.java_db_08_exercise.model.entities.Game;
import com.example.java_db_08_exercise.model.entities.Role;
import com.example.java_db_08_exercise.model.entities.User;
import com.example.java_db_08_exercise.repositories.GameRepository;
import com.example.java_db_08_exercise.repositories.UserRepository;
import com.example.java_db_08_exercise.service.GameService;
import com.example.java_db_08_exercise.service.UserService;
import com.example.java_db_08_exercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public GameServiceImpl(GameRepository gameRepository, UserService userService, UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public String addGame(GameAddDto gameAddDto) {
        if (!this.userService.isUserLoggedIn()) {
            return "No user logged in!";
        }
        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<GameAddDto>> violations = validationUtil.getViolations(gameAddDto);
        if (!violations.isEmpty()) {
            violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(m -> sb.append(m).append(System.lineSeparator()));
            return sb.toString().trim();
        }
        User loggedInUser = this.userService.findLoggedInUser();
        if (!loggedInUser.getRole().equals(Role.ADMIN)) {
            return loggedInUser.getFullName() + " is not an administrator!";
        }

        Game game = this.modelMapper.map(gameAddDto, Game.class);

        //save in DB
        this.gameRepository.save(game);

        return "Added game " + game.getTitle();
    }


    @Override
    public String editGame(long id, String[] values) {
        if (!this.userService.isUserLoggedIn()) {
            return "No user logged in!";
        }

        User loggedInUser = this.userService.findLoggedInUser();
        if (!loggedInUser.getRole().equals(Role.ADMIN)) {
            return loggedInUser.getFullName() + " is not an administrator!";
        }

        Game gameToEdit = this.gameRepository.findById(id).orElse(null);
        if (gameToEdit == null) {
            return "No game with such id!";
        } else {
            for (String value : values) {
                String parameter = value.split("=")[0];
                String newValue = value.split("=")[1];
                switch (parameter) {
                    case "title":
                        gameToEdit.setTitle(newValue);
                        break;
                    case "trailer":
                        gameToEdit.setTrailer(newValue);
                        break;
                    case "imageThumbnail":
                        gameToEdit.setImageThumbnail(newValue);
                        break;
                    case "size":
                        gameToEdit.setSize(Double.parseDouble(newValue));
                        break;
                    case "price":
                        gameToEdit.setPrice(new BigDecimal(newValue));
                        break;
                    case "description":
                        gameToEdit.setDescription(newValue);
                        break;
                    case "releaseDate":
                        gameToEdit.setReleaseDate(LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                        break;
                }
                this.gameRepository.save(gameToEdit);

            }
        }
        return "Edited " + gameToEdit.getTitle();
    }

    @Override
    public String deleteGame(long id) {
        if (!this.userService.isUserLoggedIn()) {
            return "No user logged in!";
        }

        User loggedInUser = this.userService.findLoggedInUser();
        if (!loggedInUser.getRole().equals(Role.ADMIN)) {
            return loggedInUser.getFullName() + " is not an administrator!";
        }

        Game gameToDelete = this.gameRepository.findById(id).orElse(null);
        if (gameToDelete == null) {
            System.out.println();
            return "No game with such id!";
        }

        this.gameRepository.delete(gameToDelete);
        return "Deleted " + gameToDelete.getTitle();
    }

    @Override
    public String getAllGames() {
        StringBuilder sb = new StringBuilder();
        this.gameRepository
                .findAll().stream()
                .map(game -> modelMapper.map(game, GamesTitleAndPriceDto.class))
                .forEach(g -> sb.append(String.format("%s %.2f%n", g.getTitle(), g.getPrice())));

        return sb.toString().trim();
    }


    @Override
    public String getDetailsForGame(String title) {
        GameDetailsDto game = this.modelMapper.map(this.gameRepository.findByTitle(title), GameDetailsDto.class);
        StringBuilder sb = new StringBuilder();
        sb
                .append(String.format("Title: %s%n", game.getTitle()))
                .append(String.format("Price: %.2f%n", game.getPrice()))
                .append(String.format("Description: %s%n", game.getDescription()))
                .append(String.format("Release date: %s",
                        game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        return sb.toString().trim();
    }


}
