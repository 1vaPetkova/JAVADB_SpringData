package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.seed.xml.PictureSeedDto;
import softuni.exam.domain.dto.seed.xml.PictureSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;

import java.io.IOException;

import static softuni.exam.common.Constants.*;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;

    public PictureServiceImpl(PictureRepository pictureRepository,
                              ValidationUtil validationUtil, ModelMapper modelMapper,
                              FileUtil fileUtil, XmlParser xmlParser) {
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public String importPictures() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        PictureSeedRootDto pictureSeedRootDto = this.xmlParser.parseXml(PictureSeedRootDto.class, PICTURES_PATH);
        for (PictureSeedDto pictureSeedDto : pictureSeedRootDto.getPictures()) {
            if (!validationUtil.isValid(pictureSeedDto)) {
                getIncorrectDataMessage(sb, "picture");
                continue;
            }
            Picture picture = this.modelMapper.map(pictureSeedDto, Picture.class);
            this.pictureRepository.save(picture);
            getSuccessPictureMessage(sb, pictureSeedDto.getUrl());
        }

        return sb.toString().trim();
    }

    @Override
    public boolean areImported() {

        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return this.fileUtil.readFile(PICTURES_PATH);
    }

}
