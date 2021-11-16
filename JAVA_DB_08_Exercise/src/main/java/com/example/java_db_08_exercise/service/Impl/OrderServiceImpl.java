package com.example.java_db_08_exercise.service.Impl;

import com.example.java_db_08_exercise.model.entities.Game;
import com.example.java_db_08_exercise.model.entities.Order;
import com.example.java_db_08_exercise.model.entities.User;
import com.example.java_db_08_exercise.repositories.GameRepository;
import com.example.java_db_08_exercise.repositories.OrderRepository;
import com.example.java_db_08_exercise.repositories.UserRepository;
import com.example.java_db_08_exercise.service.OrderService;
import com.example.java_db_08_exercise.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GameRepository gameRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, GameRepository gameRepository, UserService userService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public String addItem(String title) {
        User loggedInUser = this.userService.findLoggedInUser();
        if (loggedInUser == null) {
            return "No user was logged in.";
        }
        if (loggedInUser.getGames().stream().anyMatch(g -> g.getTitle().equals(title))) {
            return loggedInUser.getFullName() + " already owns " + title;
        }
        Game gameToAdd = this.gameRepository.findByTitle(title);
        Order order;
        if (this.orderRepository.findByBuyer(loggedInUser) == null
                || this.orderRepository.findByBuyer(loggedInUser).getOrderFinalized()) {
            order = new Order();
            order.setBuyer(loggedInUser);
        } else {
            order = this.orderRepository.findByBuyer(loggedInUser);
        }
        order.getGames().add(gameToAdd);
        order.setOrderFinalized(false);

        this.orderRepository.save(order);
        return gameToAdd.getTitle() + " added to cart.";
    }

    @Override
    public String removeItem(String title) {
        User loggedInUser = this.userService.findLoggedInUser();
        if (loggedInUser == null) {
            return "No user was logged in.";
        }
        Order order = this.orderRepository.findByOrderFinalizedFalse();
        Game gameToRemove = this.gameRepository.findByTitle(title);
        if (!order.getGames().contains(gameToRemove)) {
            return "No such game in shopping cart.";
        }
        order.getGames().remove(gameToRemove);
        this.orderRepository.save(order);
        return gameToRemove.getTitle() + " removed from cart.";
    }

    @Override
    public String buyItem() {
        User loggedInUser = this.userService.findLoggedInUser();
        if (loggedInUser == null) {
            return "No user was logged in.";
        }
        Order order = this.orderRepository.findByOrderFinalizedFalse();
        User user = this.userRepository.findById(loggedInUser.getId()).get();
        order.getGames().forEach(g -> user.getGames().add(g));
        this.userRepository.save(user);
        this.orderRepository.findByBuyer(loggedInUser).setOrderFinalized(true);

        StringBuilder sb = new StringBuilder("Successfully bought games:").append(System.lineSeparator());
        order.getGames().stream().map(Game::getTitle)
                .forEach(title -> sb.append(String.format(" -%s", title)).append(System.lineSeparator()));
        return sb.toString().trim();
    }
}
