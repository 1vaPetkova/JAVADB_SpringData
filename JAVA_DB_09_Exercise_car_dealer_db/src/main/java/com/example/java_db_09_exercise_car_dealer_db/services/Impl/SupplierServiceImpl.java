package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.SupplierSeedDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.SuppliersLocalDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Supplier;
import com.example.java_db_09_exercise_car_dealer_db.repositories.SupplierRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.SupplierService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.SUPPLIERS_PATH;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, Gson gson, FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedSuppliers() throws IOException {
        if (this.supplierRepository.count() == 0) {
            Arrays
                    .stream(gson.fromJson(fileUtil.readFileContent(SUPPLIERS_PATH), SupplierSeedDto[].class))
                    .filter(validationUtil::isValid)
                    .map(supplierSeedDto -> modelMapper.map(supplierSeedDto, Supplier.class))
                    .forEach(this.supplierRepository::save);
        }
    }

    @Override
    public Supplier findRandomSupplier() {
        long randomId = ThreadLocalRandom
                .current()
                .nextLong(1, this.supplierRepository.count() + 1);
        return this.supplierRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<SuppliersLocalDto> findAllByImporterIsFalse() {
        return this.supplierRepository
                .findAllByImporterIsFalse()
                .stream()
                .map(supplier -> {
                    SuppliersLocalDto suppliersLocalDto = modelMapper.map(supplier, SuppliersLocalDto.class);
                    suppliersLocalDto.setPartsCount(supplier.getParts().size());
                    return suppliersLocalDto;
                }).collect(Collectors.toList());
    }
}
