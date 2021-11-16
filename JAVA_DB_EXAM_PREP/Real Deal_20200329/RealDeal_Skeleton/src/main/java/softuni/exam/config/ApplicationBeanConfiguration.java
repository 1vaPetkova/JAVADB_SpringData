package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.util.files.FileUtilImpl;
import softuni.exam.util.validator.ValidationUtil;
import softuni.exam.util.validator.ValidationUtilImpl;
import softuni.exam.util.xmlParser.XMLParserImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<String, LocalDate> localDateConverter = new Converter<String, LocalDate>() {

            @Override
            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                return LocalDate.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        };
        modelMapper.addConverter(localDateConverter);

        Converter<String, LocalDateTime> localDateTimeConverter = new AbstractConverter<String, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(String s) {
                return
                        LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

            ;
        };
        modelMapper.addConverter(localDateTimeConverter);



        return modelMapper;
    }

    @Bean
    public FileUtilImpl fileUtil() {
        return new FileUtilImpl();
    }

    @Bean
    public XMLParserImpl xmlParser() {
        return new XMLParserImpl(fileUtil());
    }
}
