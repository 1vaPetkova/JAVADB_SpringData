package softuni.exam.instagraphlite.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileUtilImpl implements FileUtil {

    @Override
    public String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }
}