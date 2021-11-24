package exam.service;

import exam.model.dto.seed.xml.ShopSeedDto;
import exam.model.dto.seed.xml.ShopSeedRootDto;
import exam.model.entities.Shop;
import exam.model.entities.Town;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.util.FileUtil;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static exam.common.Constants.*;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    public ShopServiceImpl(ShopRepository shopRepository, TownRepository townRepository,
                           ValidationUtil validationUtil, FileUtil fileUtil, XmlParser xmlParser,
                           ModelMapper modelMapper) {
        this.shopRepository = shopRepository;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return this.fileUtil.readFile(SHOPS_PATH);
    }

    @Override
    public String importShops() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        ShopSeedRootDto shopSeedRootDto = this.xmlParser.parseXml(ShopSeedRootDto.class, SHOPS_PATH);
        for (ShopSeedDto shopSeedDto : shopSeedRootDto.getShops()) {
            Town town = this.townRepository.findByName(shopSeedDto.getTown().getName());
            if (!this.validationUtil.isValid(shopSeedDto)
                    || town == null
                    || this.shopRepository.findByName(shopSeedDto.getName()) != null) {
                getIncorrectDataMessage(sb, "shop");
                continue;
            }
            Shop shop = this.modelMapper.map(shopSeedDto, Shop.class);
            shop.setTown(town);
            this.shopRepository.save(shop);
            getSuccessShopMessage(sb, shop.getName(), shop.getIncome());

        }
        return sb.toString().trim();
    }
}
