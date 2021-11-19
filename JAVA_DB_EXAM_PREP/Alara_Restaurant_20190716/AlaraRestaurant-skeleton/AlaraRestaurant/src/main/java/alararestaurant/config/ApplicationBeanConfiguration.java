package alararestaurant.config;

import alararestaurant.util.files.FileUtil;
import alararestaurant.util.files.FileUtilImpl;
import alararestaurant.util.validator.ValidationUtil;
import alararestaurant.util.validator.ValidationUtilImpl;
import alararestaurant.util.xmlParser.XMLParser;
import alararestaurant.util.xmlParser.XMLParserImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public FileUtil fileUtil() {
        return new FileUtilImpl();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<String, LocalDateTime> localDateConverter = new Converter<String, LocalDateTime>() {

            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {
                return LocalDateTime
                        .parse(mappingContext.getSource(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
        };
        modelMapper.addConverter(localDateConverter);

        return modelMapper;
    }

    @Bean
    public XMLParser xmlParser(){
        return new XMLParserImpl(fileUtil());
    }

}
