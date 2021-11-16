package com.example.java_db_09_exercise.util.xmlParser;

import com.example.java_db_09_exercise.util.files.FileUtilImpl;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;

@Component
public class XMLParserImpl implements XMLParser {
    private JAXBContext jaxbContext;
    private final FileUtilImpl fileUtil;

    public XMLParserImpl(FileUtilImpl fileUtil) {
        this.fileUtil = fileUtil;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromFile(String filePath, Class<T> tClass) throws JAXBException, FileNotFoundException {
        jaxbContext = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(fileUtil.readFromFile(filePath));
    }

    @Override
    public <T> void writeToFile(String filePath, T entity) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(entity.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(entity, fileUtil.writeToFile(filePath));

    }
}
