package exam.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Component
public class XmlParserImpl implements XmlParser {
    private JAXBContext jaxbContext;

    public XmlParserImpl() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E parseXml(Class<E> objectClass, String filePath) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(objectClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (E) unmarshaller.unmarshal(new File(filePath));
    }
}
