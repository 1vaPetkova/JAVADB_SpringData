package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.seed.json.PictureSeedDto;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.FileUtil;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;

import static softuni.exam.instagraphlite.common.Constants.*;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final FileUtil fileUtil;

    public PictureServiceImpl(PictureRepository pictureRepository, ValidationUtil validationUtil,
                              ModelMapper modelMapper, Gson gson, FileUtil fileUtil) {
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return this.fileUtil.readFile(PICTURES_PATH);
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();
        PictureSeedDto[] pictureSeedDtos = this.gson.fromJson(readFromFileContent(), PictureSeedDto[].class);
        for (PictureSeedDto pictureSeedDto : pictureSeedDtos) {
            if (this.pictureRepository.existsByPath(pictureSeedDto.getPath())
                    || !this.validationUtil.isValid(pictureSeedDto)) {
                getIncorrectDataMessage(sb, "Picture");
                continue;
            }
            Picture picture = this.modelMapper.map(pictureSeedDto, Picture.class);
            this.pictureRepository.save(picture);
            getSuccessPictureMessage(sb, picture.getSize());
        }
        return sb.toString().trim();
    }

    @Override
    public String exportPictures() {
        StringBuilder sb = new StringBuilder();
        this.pictureRepository.findAllBySizeGreaterThanOrderBySizeAsc(30000.0)
                .forEach(picture ->
                        sb
                                .append(String.format("%.2f - %s",
                                        picture.getSize(),
                                        picture.getPath())).append(System.lineSeparator())
                );
        return sb.toString().trim();
    }
}
