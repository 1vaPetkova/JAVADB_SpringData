package com.example.java_db_07_lab.services;

import com.example.java_db_07_lab.entities.Shampoo;
import com.example.java_db_07_lab.entities.Size;
import com.example.java_db_07_lab.repositories.ShampooRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShampooServiceImpl implements ShampooService {

    private final ShampooRepository shampooRepository;

    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }

    @Override
    public List<Shampoo> findAllBySizeOrderById(Size size) {
        return this.shampooRepository.findAllBySizeOrderById(size);
    }

    @Override
    public List<Shampoo> findAllBySizeOrLabel_IdOrderByPrice(Size size, Long labelId) {
        return this.shampooRepository.findAllBySizeOrLabel_IdOrderByPrice(size, labelId);
    }

    @Override
    public List<Shampoo> findAllByPriceGreaterThanOrderByPriceDesc(BigDecimal price) {
        return this.shampooRepository.findAllByPriceGreaterThanOrderByPriceDesc(price);
    }

    @Override
    public Integer countAllByPriceLessThan(BigDecimal price) {
        return this.shampooRepository.countAllByPriceLessThan(price);
    }

    @Override
    public Set<String> findAllByIngredientsNames(List<String> ingredients) {
        return this.shampooRepository
                .findAllByIngredientsNames(ingredients)
                .stream()
                .map(Shampoo::getBrand)
                .collect(Collectors.toSet());
    }

    @Override
    public List<String> findAllByIngredientsCount(int count) {
        return this.shampooRepository
                .findAllByIngredientsCounts(count)
                .stream()
                .map(Shampoo::getBrand)
                .collect(Collectors.toList());
    }
}
