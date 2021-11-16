package com.example.java_db_09_exercise.services.Impl;

import com.example.java_db_09_exercise.model.dto.*;
import com.example.java_db_09_exercise.model.dto.Q04.ProductCountAndInfoDto;
import com.example.java_db_09_exercise.model.dto.Q04.ProductNameAndPriceDto;
import com.example.java_db_09_exercise.model.dto.Q04.SellersByCountDto;
import com.example.java_db_09_exercise.model.dto.Q04.UserInfoDto;
import com.example.java_db_09_exercise.model.entities.Product;
import com.example.java_db_09_exercise.model.entities.User;
import com.example.java_db_09_exercise.repositories.UserRepository;
import com.example.java_db_09_exercise.services.UserService;
import com.example.java_db_09_exercise.util.files.FileUtil;
import com.example.java_db_09_exercise.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise.util.files.FilePaths.USERS_PATH;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final FileUtil fileUtil;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           ValidationUtil validationUtil, Gson gson, FileUtil fileUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedUsers() throws IOException {
        if (this.userRepository.count() == 0) {
            Arrays
                    .stream(gson.fromJson(fileUtil.readFileContent(USERS_PATH), UserSeedDto[].class))
                    .filter(validationUtil::isValid)
                    .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                    .forEach(userRepository::save);
        }
    }

    @Override
    public User findRandomUser() {
        long randomId = ThreadLocalRandom
                .current()
                .nextLong(1, this.userRepository.count() + 1);
        return this.userRepository.findById(randomId).orElse(null);
    }

    //Q02
    @Override
    public List<UsersSoldProductsDto> findAllUsersByMoreThanOneSoldProductsOrderByLastNameThenFirstName() {
        return this.userRepository
                .findAllUsersByMoreThanOneSoldProductsOrderByLastNameThenFirstName()
                .stream()
                .map(user -> modelMapper.map(user, UsersSoldProductsDto.class))
                .collect(Collectors.toList());
    }

    //Q04
    @Override
    public SellersByCountDto findAllUsersWithSoldProductsOrderByProductsCountThenByLastName() {
        //findAllUsersWhichHaveAtLeastOneProductSold
        List<User> allSellers = this.userRepository
                .findAllUsersWithSoldProductsOrderByProductsCountThenByLastName();
        Set<UserInfoDto> userInfoDtos = new HashSet<>();
        for (User user : allSellers) {
            //Map users to UserInfo
            UserInfoDto userInfo = modelMapper.map(user, UserInfoDto.class);
            Set<Product> productsSold = user.getSoldProducts(); //set of the products that user has sold
            Set<ProductNameAndPriceDto> soldProducts = productsSold
                    .stream()
                    .map(product -> modelMapper.map(product, ProductNameAndPriceDto.class))
                    .collect(Collectors.toSet());

            ProductCountAndInfoDto productCountAndInfoDto = new ProductCountAndInfoDto();
            productCountAndInfoDto.setProducts(soldProducts);
            productCountAndInfoDto.setCount();
            userInfo.setSoldProducts(productCountAndInfoDto);
            userInfoDtos.add(userInfo);
        }
        SellersByCountDto sellersByCountDto = new SellersByCountDto();
        sellersByCountDto.setUsers(userInfoDtos);
        sellersByCountDto.setUsersCount();
        return sellersByCountDto;
    }


}

