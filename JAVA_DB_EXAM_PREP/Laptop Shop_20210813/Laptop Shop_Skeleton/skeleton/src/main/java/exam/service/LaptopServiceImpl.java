package exam.service;

import com.google.gson.Gson;
import exam.model.dto.seed.json.LaptopSeedDto;
import exam.model.entities.Laptop;
import exam.model.entities.Shop;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.util.FileUtil;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static exam.common.Constants.*;

@Service
public class LaptopServiceImpl implements LaptopService {

    private final LaptopRepository laptopRepository;
    private final ShopRepository shopRepository;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopRepository shopRepository,
                             ValidationUtil validationUtil, FileUtil fileUtil,
                             Gson gson, ModelMapper modelMapper) {
        this.laptopRepository = laptopRepository;
        this.shopRepository = shopRepository;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return this.fileUtil.readFile(LAPTOPS_PATH);
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder sb = new StringBuilder();
        LaptopSeedDto[] laptopSeedDtos = this.gson.fromJson(readLaptopsFileContent(), LaptopSeedDto[].class);
        for (LaptopSeedDto laptopSeedDto : laptopSeedDtos) {
            Shop shop = this.shopRepository.findByName(laptopSeedDto.getShop().getName());
            if (!this.validationUtil.isValid(laptopSeedDto)
                    || shop == null
                    || this.laptopRepository.findByMacAddress(laptopSeedDto.getMacAddress()) != null) {
                getIncorrectDataMessage(sb, "laptop");
                continue;
            }
            Laptop laptop = this.modelMapper.map(laptopSeedDto, Laptop.class);
            laptop.setShop(shop);
            this.laptopRepository.save(laptop);
            getSuccessLaptopMessage(sb, laptop.getMacAddress(), laptop.getCpuSpeed(), laptop.getRam(), laptop.getStorage());
        }
        return sb.toString().trim();
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder sb = new StringBuilder();
        this.laptopRepository
                .findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddress()
                .forEach(l->sb.append(l.toString()).append(System.lineSeparator()));
        return sb.toString().trim();
    }
}
