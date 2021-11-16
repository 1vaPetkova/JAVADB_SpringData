package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.PartSeedDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import com.example.java_db_09_exercise_car_dealer_db.repositories.PartRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.PartService;
import com.example.java_db_09_exercise_car_dealer_db.services.SupplierService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.PARTS_PATH;

@Service
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final SupplierService supplierService;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public PartServiceImpl(PartRepository partRepository, SupplierService supplierService, Gson gson, FileUtil fileUtil,
                           ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.partRepository = partRepository;
        this.supplierService = supplierService;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedParts() throws IOException {
        if (this.partRepository.count() == 0) {
            Arrays
                    .stream(gson.fromJson(fileUtil.readFileContent(PARTS_PATH), PartSeedDto[].class))
                    .filter(validationUtil::isValid)
                    .map(partSeedDto -> {
                        Part part = modelMapper.map(partSeedDto, Part.class);
                        part.setSupplier(this.supplierService.findRandomSupplier());
                        return part;
                    })
                    .forEach(this.partRepository::save);
        }
    }

    @Override
    public Set<Part> findRandomParts() {
        Set<Part> parts = new HashSet<>();
        long partsCount = ThreadLocalRandom
                .current()
                .nextLong(3, 6);

        List<Long> partIds = this.partRepository
                .findAll().stream()
                .map(Part::getId).collect(Collectors.toList());
        Collections.shuffle(partIds);
        for (int i = 0; i < partsCount; i++) {
            parts.add(this.partRepository.findById(partIds.get(i)).orElse(null));
        }
        return parts;
    }
}
