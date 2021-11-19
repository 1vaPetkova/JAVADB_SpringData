package alararestaurant.service;

import alararestaurant.domain.dtos.seed.json.ItemSeedDto;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.util.files.FilePaths;
import alararestaurant.util.files.FileUtil;
import alararestaurant.util.validator.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository,
                           FileUtil fileUtil,
                           Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean itemsAreImported() {
        return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {
        return this.fileUtil.readFile(FilePaths.ITEMS_PATH);
    }

    @Override
    public String importItems(String items) {
        ItemSeedDto[] itemSeedDtos = this.gson.fromJson(items, ItemSeedDto[].class);
        StringBuilder sb = new StringBuilder();
        for (ItemSeedDto itemSeedDto : itemSeedDtos) {

            if (!this.validationUtil.isValid(itemSeedDto)
                    || this.itemRepository.findByName(itemSeedDto.getName()) != null) {
                getErrorMessage(sb);
                continue;
            }
            Category category = this.categoryRepository.findByName(itemSeedDto.getCategory());
            if (category == null) {
                category = new Category(itemSeedDto.getCategory());
                category = this.categoryRepository.save(category);//!!!!!!!!!!!!! reference!
            }
            if (!this.validationUtil.isValid(category)) {
                getErrorMessage(sb);
            }
            Item item = this.modelMapper.map(itemSeedDto, Item.class);
            item.setCategory(category);
            this.itemRepository.save(item);
            getSuccessfulMessage(sb, itemSeedDto);
        }
        return sb.toString();
    }

    private void getErrorMessage(StringBuilder sb) {
        sb
                .append("Invalid data format.")
                .append(System.lineSeparator());
    }

    private void getSuccessfulMessage(StringBuilder sb, ItemSeedDto itemSeedDto) {
        sb
                .append(String.format("Record %s successfully imported.", itemSeedDto.getName()))
                .append(System.lineSeparator());
    }
}
