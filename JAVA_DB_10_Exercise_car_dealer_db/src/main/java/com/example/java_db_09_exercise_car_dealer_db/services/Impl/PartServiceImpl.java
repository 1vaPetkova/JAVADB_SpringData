package com.example.java_db_09_exercise_car_dealer_db.services.Impl;

import com.example.java_db_09_exercise_car_dealer_db.model.dto.seed.PartSeedRootDto;
import com.example.java_db_09_exercise_car_dealer_db.model.entities.Part;
import com.example.java_db_09_exercise_car_dealer_db.repositories.PartRepository;
import com.example.java_db_09_exercise_car_dealer_db.services.PartService;
import com.example.java_db_09_exercise_car_dealer_db.services.SupplierService;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.validator.ValidationUtil;
import com.example.java_db_09_exercise_car_dealer_db.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.java_db_09_exercise_car_dealer_db.util.files.FilePaths.PARTS_PATH;

@Service
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final SupplierService supplierService;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;

    public PartServiceImpl(PartRepository partRepository, SupplierService supplierService, FileUtil fileUtil,
                           ValidationUtil validationUtil, ModelMapper modelMapper, XMLParser xmlParser) {
        this.partRepository = partRepository;
        this.supplierService = supplierService;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }


    @Override
    public void seedParts() throws IOException, JAXBException {
        if (this.partRepository.count() == 0) {
            PartSeedRootDto partSeedRootDto = this.xmlParser.fromFile(PARTS_PATH, PartSeedRootDto.class);
            partSeedRootDto.getParts()
                    .stream()
                    .filter(this.validationUtil::isValid)
                    .map(partSeedDto -> {
                        Part part = this.modelMapper.map(partSeedDto, Part.class);
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
