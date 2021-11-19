package alararestaurant.util.xmlParser;

import alararestaurant.util.files.FileUtil;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

@Component
public class XMLParserImpl implements XMLParser {
    private JAXBContext jaxbContext;
    private final FileUtil fileUtil;

    public XMLParserImpl(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T parseXml(String filePath, Class<T> tClass) throws JAXBException {
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
