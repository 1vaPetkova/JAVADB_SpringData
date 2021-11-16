package com.example.java_db_08_exercise.configurations;

import com.example.java_db_08_exercise.model.dto.GameAddDto;
import com.example.java_db_08_exercise.model.dto.UserGamesDto;
import com.example.java_db_08_exercise.model.entities.Game;
import com.example.java_db_08_exercise.model.entities.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<String, LocalDate> localDateConverter = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String s) {
                return s == null ?
                        LocalDate.now() :
                        LocalDate.parse(s, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        };
        modelMapper.addConverter(localDateConverter);

        modelMapper.typeMap(GameAddDto.class, Game.class)
                .addMappings(mapper ->
                        mapper.map(GameAddDto::getThumbnailURL, Game::setImageThumbnail));


        Converter<Set<Game>, Set<String>> SetGameToStringConverter = new AbstractConverter<Set<Game>, Set<String>>() {
            @Override
            protected Set<String> convert(Set<Game> games) {
                return games == null ?
                        new HashSet<>()
                        : games.stream().map(Game::getTitle).collect(Collectors.toSet());
            }
        };

        modelMapper.typeMap(User.class, UserGamesDto.class)
                .addMappings(mapper ->
                        mapper.using(SetGameToStringConverter)
                                .map(User::getGames, UserGamesDto::setTitles));
        return modelMapper;
    }
}
