package com.example.java_db_08_lab.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FormatConverterFactoryImpl implements FormatConverterFactory {

    private final FormatConverter xml;
    private final FormatConverter json;
   // private final ApplicationContext ctx;

    public FormatConverterFactoryImpl(@Qualifier("xml_format_converter") FormatConverter xml,
                                      @Qualifier("json_format_converter") FormatConverter json) {
        this.xml = xml;
        this.json = json;
     //   this.ctx = ctx;
    }

    @Override
    public FormatConverter create(String formatType) {
//        String className = formatType.toUpperCase() + FormatConverter.class.getSimpleName();
//        try {
//          return(FormatConverter)  this.ctx.getBean(Class.forName(FormatConverter.class.getPackageName() + "." + className));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        switch (formatType.toLowerCase()) {
            case "xml":
                return this.xml;
            case "json":
                return this.json;
            default:
                return null;
        }
    }
}
