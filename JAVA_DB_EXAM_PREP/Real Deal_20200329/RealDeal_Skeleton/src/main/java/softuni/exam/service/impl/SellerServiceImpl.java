package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.seed.SellerSeedDto;
import softuni.exam.models.dtos.seed.SellerSeedRootDto;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.files.FileUtilImpl;
import softuni.exam.util.validator.ValidationUtil;
import softuni.exam.util.xmlParser.XMLParserImpl;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static softuni.exam.util.files.FilePaths.SELLERS_PATH;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final XMLParserImpl xmlParser;
    private final FileUtilImpl fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public SellerServiceImpl(SellerRepository sellerRepository, XMLParserImpl xmlParser,
                             FileUtilImpl fileUtil, ValidationUtil validationUtil,
                             ModelMapper modelMapper) {
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return this.fileUtil.readFileContent(SELLERS_PATH);
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        SellerSeedRootDto sellerSeedRootDto = this.xmlParser.fromFile(SELLERS_PATH, SellerSeedRootDto.class);
        sellerSeedRootDto.getSellers()
                .stream()
                .filter(sellerSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(sellerSeedDto);
                    sb
                            .append(isValid ?
                                    getSuccessMessage(sellerSeedDto) :
                                    "Invalid seller")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(sellerSeedDto ->  this.modelMapper.map(sellerSeedDto, Seller.class))
                .forEach(this.sellerRepository::save);
        return sb.toString();
    }

    private String getSuccessMessage(SellerSeedDto sellerSeedDto) {
        return String.format("Successfully import seller %s - %s",
                sellerSeedDto.getLastName(), sellerSeedDto.getEmail());
    }

    @Override
    public Seller findById(Long id) {
        return this.sellerRepository.findById(id).orElse(null);
    }
}
