package com.example.java_db_08_exercise.service.Impl;

import com.example.java_db_08_exercise.model.dto.UserGamesDto;
import com.example.java_db_08_exercise.model.dto.UserLoginDto;
import com.example.java_db_08_exercise.model.dto.UserRegisterDto;
import com.example.java_db_08_exercise.model.entities.Role;
import com.example.java_db_08_exercise.model.entities.User;
import com.example.java_db_08_exercise.repositories.UserRepository;
import com.example.java_db_08_exercise.service.UserService;
import com.example.java_db_08_exercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public String registerUser(UserRegisterDto userRegisterDto) {
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            return "Passwords do not match!";
        }
        Set<ConstraintViolation<UserRegisterDto>> violations = validationUtil.getViolations(userRegisterDto);

        StringBuilder sb = new StringBuilder();
        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(m -> sb.append(m).append(System.lineSeparator()));
            return sb.toString().trim();
        }

        //Map dto to entity and save in DB
        User user = this.modelMapper.map(userRegisterDto, User.class);

        //Check if user already registered
        User userInDB = this.userRepository
                .findByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElse(null);

        if (userInDB != null) {
            return "User is already registered";
        }

        //Set the user role
        if (this.userRepository.count() == 0) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }
        userRepository.save(user);
        return user.getFullName() + " was registered";
    }

    @Override
    public String loginUser(UserLoginDto userLoginDto) {
        Set<ConstraintViolation<UserLoginDto>> violations = validationUtil.getViolations(userLoginDto);
        StringBuilder sb = new StringBuilder();
        if (!violations.isEmpty()) {
            violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(m -> sb.append(m).append(System.lineSeparator()));
            return sb.toString().trim();
        }
        if (loggedInUser != null) {
            return loggedInUser.getEmail() + " is already logged in.";
        }

        User user = this.userRepository
                .findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword())
                .orElse(null);

        if (user == null) {
            return "Incorrect username / password";
        }

        loggedInUser = user;
        return "Successfully logged in " + loggedInUser.getFullName();
    }

    @Override
    public String logout() {
        if (loggedInUser == null) {
            return "Cannot log out. No user was logged in.";
        }
        String name = loggedInUser.getFullName();
        loggedInUser = null;
        return "User " + name + "successfully logged out.";
    }

    @Override
    public boolean isUserLoggedIn() {
        return this.loggedInUser != null;
    }

    @Override
    public String getOwnedGames() {
        User byId = this.userRepository.findById(findLoggedInUser().getId()).orElse(null);
        UserGamesDto user = this.modelMapper.map(byId, UserGamesDto.class);
        StringBuilder sb = new StringBuilder();
        user.getTitles().forEach(t->sb.append(t).append(System.lineSeparator()));
        return sb.toString().trim();
    }

    @Override
    public User findLoggedInUser() {
        return loggedInUser;
    }

}
