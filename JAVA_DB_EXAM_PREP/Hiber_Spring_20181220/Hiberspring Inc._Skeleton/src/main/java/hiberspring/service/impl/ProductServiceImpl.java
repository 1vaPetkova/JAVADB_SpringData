package hiberspring.service.impl;

import hiberspring.domain.dtos.seed.xml.ProductSeedDto;
import hiberspring.domain.dtos.seed.xml.ProductSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.ProductRepository;
import hiberspring.service.ProductService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public ProductServiceImpl(ProductRepository productRepository, BranchRepository branchRepository, FileUtil fileUtil,
                              ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return this.fileUtil.readFile(PRODUCTS_PATH);
    }

    @Override
    public String importProducts() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        ProductSeedRootDto productSeedRootDto = this.xmlParser.parseXml(ProductSeedRootDto.class, PRODUCTS_PATH);
        for (ProductSeedDto productSeedDto : productSeedRootDto.getProducts()) {
            Branch branch = this.branchRepository.findByName(productSeedDto.getBranch());
            if (!this.validationUtil.isValid(productSeedDto)
                    || branch == null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Product product = this.modelMapper.map(productSeedDto, Product.class);
            product.setBranch(branch);
            this.productRepository.save(product);
            getSuccessProductMessage(sb,product.getName());
        }
        return sb.toString().trim();
    }
}
