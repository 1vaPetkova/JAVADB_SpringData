package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.seed.xml.PlaneSeedDto;
import softuni.exam.models.dto.seed.xml.PlaneSeedRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static softuni.exam.common.Constants.*;

@Service
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public PlaneServiceImpl(PlaneRepository planeRepository, FileUtil fileUtil,
                            ValidationUtil validationUtil, ModelMapper modelMapper,
                            XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return this.fileUtil.readFile(PLANES_PATH);
    }

    @Override
    public String importPlanes() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        PlaneSeedRootDto planeSeedRootDto = this.xmlParser.parseXml(PlaneSeedRootDto.class, PLANES_PATH);
        for (PlaneSeedDto planeSeedDto : planeSeedRootDto.getPlanes()) {
            if (!this.validationUtil.isValid(planeSeedDto)) {
                getIncorrectDataMessage(sb, "Plane");
                continue;
            }
            Plane plane = this.modelMapper.map(planeSeedDto, Plane.class);
            this.planeRepository.save(plane);
            getSuccessPlaneMessage(sb, plane.getRegisterNumber());
        }
        return sb.toString().trim();
    }
}
