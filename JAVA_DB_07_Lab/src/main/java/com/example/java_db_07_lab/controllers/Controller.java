package com.example.java_db_07_lab.controllers;

import com.example.java_db_07_lab.entities.Size;
import com.example.java_db_07_lab.services.IngredientService;
import com.example.java_db_07_lab.services.ShampooService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Controller implements CommandLineRunner {

    private final ShampooService shampooService;
    private final IngredientService ingredientService;
    private final Scanner scan = new Scanner(System.in);

    public Controller(ShampooService shampooService, IngredientService ingredientService) {
        this.shampooService = shampooService;
        this.ingredientService = ingredientService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Enter problem number:");
        int problem = Integer.parseInt(scan.nextLine());
        switch (problem) {
            case 1:
                P01SelectShampoosBySize();
                break;
            case 2:
                P02SelectShampoosBySizeOrLabel();
                break;
            case 3:
                P03SelectShampoosByPrice();
                break;
            case 4:
                P04SelectIngredientsByName();
                break;
            case 5:
                P05SelectIngredientsByNameFromAList();
                break;
            case 6:
                P06CountShampoosByPrice();
                break;
            case 7:
                P07SelectShampoosByIngredients();
                break;
            case 8:
                P08SelectShampoosByIngredientsCount();
                break;
            case 9:
                P09DeleteIngredientsByName();
                break;
            case 10:
                P10UpdateIngredientsByPrice();
                break;
            case 11:
                P11UpdateIngredientsByNames();
                break;
        }

    }

    private void P11UpdateIngredientsByNames() {
        System.out.println("Enter increase percentage:");
        int percent = Integer.parseInt(scan.nextLine());
        System.out.println("Select ingredients to update");
        List<String> ingredients = Arrays.stream(scan.nextLine().split(",\\s+"))
                .collect(Collectors.toList());
        System.out.println("Changed: " + this.ingredientService.updateIngredientsPrice(percent, ingredients));
    }

    private void P10UpdateIngredientsByPrice() {
        System.out.println("Enter increase percentage:");
        int percent = Integer.parseInt(scan.nextLine());
        System.out.println("Changed: " + this.ingredientService.updateIngredientsPrice(percent));
    }

    private void P09DeleteIngredientsByName() {
        System.out.println("Enter ingredient to delete:");
        String ingredient = scan.nextLine();
        this.ingredientService.deleteAllByName(ingredient);
        System.out.println("Deleted ingredient: " + ingredient);

    }

    private void P08SelectShampoosByIngredientsCount() {
        System.out.println("Enter ingredients count:");
        int count = Integer.parseInt(scan.nextLine());
        this.shampooService
                .findAllByIngredientsCount(count)
                .forEach(System.out::println);
    }

    private void P07SelectShampoosByIngredients() {
        System.out.println("Enter ingredients:");
        List<String> ingredients = Arrays.stream(scan.nextLine().split("\\s+"))
                .collect(Collectors.toList());
        this.shampooService
                .findAllByIngredientsNames(ingredients)
                .forEach(System.out::println);
    }

    private void P06CountShampoosByPrice() {
        System.out.println("Enter price:");
        BigDecimal price = new BigDecimal(scan.nextLine());
        System.out.println(this.shampooService.countAllByPriceLessThan(price));
    }

    private void P05SelectIngredientsByNameFromAList() {
        System.out.println("Enter ingredients");
        List<String> names = Arrays.stream(scan.nextLine().split("\\s+"))
                .collect(Collectors.toList());
        this.ingredientService
                .findAllByNameInOrderByPrice(names)
                .forEach(System.out::println);
    }

    private void P04SelectIngredientsByName() {
        System.out.println("Enter ingredient letters:");
        String pattern = scan.nextLine();
        this.ingredientService
                .findAllByNameStartsWith(pattern)
                .forEach(i -> System.out.println(i.getName()));
    }

    private void P03SelectShampoosByPrice() {
        System.out.println("Enter minimum price:");
        BigDecimal price = new BigDecimal(scan.nextLine());
        this.shampooService.findAllByPriceGreaterThanOrderByPriceDesc(price)
                .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                        s.getBrand(), s.getSize(), s.getPrice()));
    }

    private void P02SelectShampoosBySizeOrLabel() {
        System.out.println("Enter size and label id:");
        Size size = Size.valueOf(scan.nextLine());
        Long labelId = Long.parseLong(scan.nextLine());
        this.shampooService.findAllBySizeOrLabel_IdOrderByPrice(size, labelId)
                .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                        s.getBrand(), s.getSize(), s.getPrice()));

    }

    private void P01SelectShampoosBySize() {
        System.out.println("Enter size:");
        Size size = Size.valueOf(scan.nextLine());
        this.shampooService
                .findAllBySizeOrderById(size)
                .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                        s.getBrand(), s.getSize(), s.getPrice()));
    }
}
