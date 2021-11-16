package com.example.java_db_09_exercise.services.Impl;

import com.example.java_db_09_exercise.model.dto.views.Q02.ProductSoldDto;
import com.example.java_db_09_exercise.model.dto.views.Q02.UserRootSoldProducts;
import com.example.java_db_09_exercise.model.dto.views.Q04.*;
import com.example.java_db_09_exercise.model.dto.views.Q02.UsersSoldProductsDto;
import com.example.java_db_09_exercise.model.dto.seed.UserSeedRootDto;
import com.example.java_db_09_exercise.model.entities.Product;
import com.example.java_db_09_exercise.model.entities.User;
import com.example.java_db_09_exercise.repositories.UserRepository;
import com.example.java_db_09_exercise.services.UserService;
import com.example.java_db_09_exercise.util.files.FileUtil;
import com.example.java_db_09_exercise.util.validator.ValidationUtil;
import com.example.java_db_09_exercise.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise.util.files.FilePaths.USERS_PATH;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XMLParser xmlParser;
    private final FileUtil fileUtil;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           ValidationUtil validationUtil, XMLParser xmlParser, FileUtil fileUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedUsers() throws IOException, JAXBException {
        if (this.userRepository.count() == 0) {
            UserSeedRootDto usersSeedRootDto = this.xmlParser
                    .fromFile(USERS_PATH, UserSeedRootDto.class);
            usersSeedRootDto.getUsers()
                    .stream()
                    .filter(this.validationUtil::isValid)
                    .map(userSeedDto -> this.modelMapper.map(userSeedDto, User.class))
                    .forEach(this.userRepository::save);
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
    public UserRootSoldProducts findAllUsersByMoreThanOneSoldProductsOrderByLastNameThenFirstName() {
        UserRootSoldProducts result = new UserRootSoldProducts();
        result.setProducts(this.userRepository
                .findAllUsersBySoldProductsOrderByLastNameThenFirstName()
                .stream()
                .map(user -> {
                    UsersSoldProductsDto usersSoldProductsDto = modelMapper.map(user, UsersSoldProductsDto.class);
                    Set<ProductSoldDto> productSoldDtos = new HashSet<>();
                    for (ProductSoldDto soldProduct : usersSoldProductsDto.getSoldProducts()) {
                        productSoldDtos.add(this.modelMapper.map(soldProduct, ProductSoldDto.class));
                    }
                    usersSoldProductsDto.setSoldProducts(productSoldDtos);
                    return usersSoldProductsDto;
                })
                .collect(Collectors.toList()));
        return result;
    }

    //Q04
    @Override
    public UsersWithSoldProductsRootDto findAllUsersWithSoldProductsOrderByProductsCountThenByLastName() {
        UsersWithSoldProductsRootDto result = new UsersWithSoldProductsRootDto();

        //findAllUsersWhichHaveAtLeastOneProductSold
        List<User> allSellers = this.userRepository
                .findAllUsersWithSoldProductsOrderByProductsCountThenByLastName();
        List<UserInfoDto> userInfoDtos = new ArrayList<>();
        for (User user : allSellers) {
            //Map users to UserInfo
            UserInfoDto userInfo = this.modelMapper.map(user, UserInfoDto.class);
            Set<Product> productsSold = user.getSoldProducts(); //set of the products that user has sold
            Set<ProductNameAndPriceDto> soldProducts = productsSold
                    .stream()
                    .map(product -> this.modelMapper.map(product, ProductNameAndPriceDto.class))
                    .collect(Collectors.toSet());
            ProductCountAndInfoDto productCountAndInfoDto = new ProductCountAndInfoDto();
            productCountAndInfoDto.setProducts(soldProducts);
            productCountAndInfoDto.setCount();
            userInfo.setSoldProducts(productCountAndInfoDto);
            userInfoDtos.add(userInfo);
        }
        result.setUsers(userInfoDtos);
        result.setCount();
        return result;
    }


}

