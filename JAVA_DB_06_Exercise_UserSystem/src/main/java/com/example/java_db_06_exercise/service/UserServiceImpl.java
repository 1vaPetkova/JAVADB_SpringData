package com.example.java_db_06_exercise.service;

import com.example.java_db_06_exercise.model.enitities.User;
import com.example.java_db_06_exercise.repository.UserRepository;
import com.example.java_db_06_exercise.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void persistUser(User user) {
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public List<String> getUsersByEmailProvider(String provider) {
        return this.userRepository
                .findAllByEmailEndingWith(provider)
                .stream()
                .map(u -> String.format("%s %s", u.getUsername(), u.getEmail())).collect(Collectors.toList());
    }

    @Override
    public int removeInactiveUsers(LocalDate date) {
        this.userRepository.findAllByLastTimeLoggedInBefore(date)
                .forEach(u -> u.setDeleted(true));
      List<User> allByIsDeletedTrue = this.userRepository.findAllByDeletedTrue();
  return allByIsDeletedTrue.size();
    }

}
