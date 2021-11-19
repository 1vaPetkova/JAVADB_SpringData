package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.seed.PictureSeedDto;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.files.FileUtilImpl;
import softuni.exam.util.validator.ValidationUtil;

import java.io.IOException;
import java.util.Arrays;

import static softuni.exam.util.files.FilePaths.PICTURES_PATH;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final FileUtilImpl fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final CarService carService;

    public PictureServiceImpl(PictureRepository pictureRepository, Gson gson,
                              FileUtilImpl fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper,
                             CarService carService) {
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.carService = carService;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return this.fileUtil.readFileContent(PICTURES_PATH);
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();
        PictureSeedDto[] pictureSeedDtos = this.gson.fromJson(readPicturesFromFile(), PictureSeedDto[].class);
        Arrays
                .stream(pictureSeedDtos)
                .filter(pictureSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(pictureSeedDto);
                    sb
                            .append(isValid ?
                                    getSuccessMessage(pictureSeedDto) :
                                    "Invalid picture")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(pictureSeedDto -> {
                    Picture picture = this.modelMapper.map(pictureSeedDto, Picture.class);
                    picture.setCar(this.carService.findById(pictureSeedDto.getCar()));
                    return picture;
                })
                .forEach(this.pictureRepository::save);
        return sb.toString();
    }

    private String getSuccessMessage(PictureSeedDto pictureSeedDto) {
        return String.format("Successfully imported picture - %s", pictureSeedDto.getName());
    }

}
