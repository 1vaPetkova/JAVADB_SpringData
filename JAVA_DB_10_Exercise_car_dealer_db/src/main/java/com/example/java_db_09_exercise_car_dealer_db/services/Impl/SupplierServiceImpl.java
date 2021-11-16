package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.SupplierSeedRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q03.SuppliersLocalDto;
import com.example.java_db_09_exercise_car_dealer_db.model.dto.view.Q03.SuppliersLocalRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Supplier;
import com.example.java_db_09_exercise_car_dealer_db.repositories.SupplierRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.SupplierService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.SUPPLIERS_PATH;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;

    public SupplierServiceImpl(SupplierRepository supplierRepository, FileUtil fileUtil,
                               ValidationUtil validationUtil, ModelMapper modelMapper, XMLParser xmlParser) {
        this.supplierRepository = supplierRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public void seedSuppliers() throws IOException, JAXBException {
        if (this.supplierRepository.count() == 0) {
            SupplierSeedRootDto supplierSeedRootDto = this.xmlParser
                    .fromFile(SUPPLIERS_PATH, SupplierSeedRootDto.class);
            supplierSeedRootDto.getSuppliers()
                    .stream()
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

    //Q03
    @Override
    public SuppliersLocalRootDto findAllByImporterIsFalse() {
        SuppliersLocalRootDto suppliersLocalRootDto = new SuppliersLocalRootDto();
        suppliersLocalRootDto.setSuppliers(this.supplierRepository
                .findAllByImporterIsFalse()
                .stream()
                .map(supplier -> {
                    SuppliersLocalDto suppliersLocalDto = modelMapper.map(supplier, SuppliersLocalDto.class);
                    suppliersLocalDto.setPartsCount(supplier.getParts().size());
                    return suppliersLocalDto;
                }).collect(Collectors.toList()));
        return suppliersLocalRootDto;
    }
}
