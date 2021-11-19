package alararestaurant.util.xmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XMLParser {

    <T> T parseXml(String filePath, Class<T> tClass) throws JAXBException, FileNotFoundException;

    <T> void writeToFile(String filePath, T entity) throws JAXBException;
}
