package com.example.java_db_09_exercise_car_dealer_db.config;

import com.example.java_db_09_exercise_car_dealer_db.model.entities.Customer;
import com.example.java_db_09_exercise_car_dealer_db.util.files.FileUtilImpl;
import com.example.java_db_09_exercise_car_dealer_db.util.xmlParser.XMLParserImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public BufferedReader reader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    @Bean
    public FileUtilImpl fileUtil() {
        return new FileUtilImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<String, LocalDateTime> localDateConverter = new AbstractConverter<String, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(String s) {
                return s == null ?
                        LocalDateTime.now() :
                        LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        };
        modelMapper.addConverter(localDateConverter);

        Converter<LocalDateTime, String> localDateToStringConverter = new AbstractConverter<LocalDateTime, String>() {
            @Override
            protected String convert(LocalDateTime localDate) {
                return localDate == null ? "" : localDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        };
        modelMapper.addConverter(localDateToStringConverter);

        Converter<Customer, String> customerToCustomerName = new AbstractConverter<Customer, String>() {
            @Override
            protected String convert(Customer customer) {
                return customer == null ? "" : customer.getName();
            }
        };
        modelMapper.addConverter(customerToCustomerName);
        return modelMapper;
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Bean
    public XMLParserImpl xmlParser() {
        return new XMLParserImpl(fileUtil());
    }
}
