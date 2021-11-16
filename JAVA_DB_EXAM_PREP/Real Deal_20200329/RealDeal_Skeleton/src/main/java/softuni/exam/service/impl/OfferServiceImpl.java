package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.seed.OfferSeedDto;
import softuni.exam.models.dtos.seed.OfferSeedRootDto;
import softuni.exam.models.entities.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.files.FileUtilImpl;
import softuni.exam.util.validator.ValidationUtil;
import softuni.exam.util.xmlParser.XMLParserImpl;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static softuni.exam.util.files.FilePaths.OFFERS_PATH;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final XMLParserImpl xmlParser;
    private final FileUtilImpl fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final CarService carService;
    private final SellerService sellerService;

    public OfferServiceImpl(OfferRepository offerRepository, XMLParserImpl xmlParser,
                            FileUtilImpl fileUtil, ValidationUtil validationUtil,
                            ModelMapper modelMapper, CarService carService,
                            SellerService sellerService) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.carService = carService;
        this.sellerService = sellerService;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return this.fileUtil.readFileContent(OFFERS_PATH);
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        OfferSeedRootDto offerSeedRootDto = this.xmlParser.fromFile(OFFERS_PATH, OfferSeedRootDto.class);
        offerSeedRootDto.getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(offerSeedDto);
                    sb
                            .append(isValid ?
                                    getSuccessMessage(offerSeedDto) :
                                    "Invalid offer")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(offerSeedDto -> {
                    Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
                    offer.setCar(this.carService.findById(offerSeedDto.getCar().getId()));
                    offer.setSeller(this.sellerService.findById(offerSeedDto.getSeller().getId()));
                    return offer;
                })
                .forEach(this.offerRepository::save);
        return sb.toString();
    }

    private String getSuccessMessage(OfferSeedDto offerSeedDto) {
        return String.format("Successfully import offer %s - %s",
                offerSeedDto.getAddedOn(),
                offerSeedDto.getHasGoldStatus());
    }

}
