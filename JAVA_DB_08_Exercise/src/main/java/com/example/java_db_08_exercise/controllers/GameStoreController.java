package com.example.java_db_08_exercise.controllers;

import com.example.java_db_08_exercise.model.dto.GameAddDto;
import com.example.java_db_08_exercise.model.dto.UserLoginDto;
import com.example.java_db_08_exercise.model.dto.UserRegisterDto;
import com.example.java_db_08_exercise.service.GameService;
import com.example.java_db_08_exercise.service.OrderService;
import com.example.java_db_08_exercise.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class GameStoreController implements CommandLineRunner {

    private final BufferedReader reader;
    private final UserService userService;
    private final GameService gameService;
    private final OrderService orderService;

    public GameStoreController(UserService userService, GameService gameService, OrderService orderService) {
        this.userService = userService;
        this.gameService = gameService;
        this.orderService = orderService;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.println("Enter commands: ");
            String[] tokens = reader.readLine().split("\\|");
            switch (tokens[0]) {
                case "RegisterUser" -> System.out.println(
                        this.userService
                                .registerUser(new UserRegisterDto(
                                        tokens[1],
                                        tokens[2],
                                        tokens[3],
                                        tokens[4]))
                );
                case "LoginUser" -> System.out.println(
                        this.userService
                                .loginUser(new UserLoginDto(
                                        tokens[1],
                                        tokens[2]))
                );
                case "Logout" -> System.out.println(this.userService.logout());
                case "AddGame" -> System.out.println(this.gameService
                        .addGame(
                                new GameAddDto(tokens[1],               //title
                                        new BigDecimal(tokens[2]),      //price
                                        Double.parseDouble(tokens[3]),  //size
                                        tokens[4],                      //trailer
                                        tokens[5],                      //thumbnailURL
                                        tokens[6],                      //description
                                        LocalDate.parse(tokens[7],
                                                DateTimeFormatter.ofPattern("dd-MM-yyyy")))));//releaseDate
                case "EditGame" -> System.out.println(
                        this.gameService
                        .editGame(Long.parseLong(tokens[1]),
                                Arrays.copyOfRange(tokens, 2, tokens.length)));
                case "DeleteGame" -> System.out.println(this.gameService.deleteGame(Long.parseLong(tokens[1])));
                case "AllGames" -> System.out.println(this.gameService.getAllGames());
                case "DetailsGame" -> System.out.println(this.gameService.getDetailsForGame(tokens[1]));
                case "OwnedGames" -> System.out.println(this.userService.getOwnedGames());
                case "AddItem" -> System.out.println(this.orderService.addItem(tokens[1]));
                case "RemoveItem" -> System.out.println(this.orderService.removeItem(tokens[1]));
                case "BuyItem" -> System.out.println(this.orderService.buyItem());
            }

        }
    }
}

