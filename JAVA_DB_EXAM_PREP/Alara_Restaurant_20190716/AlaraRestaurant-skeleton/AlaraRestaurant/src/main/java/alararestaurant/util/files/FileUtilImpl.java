package alararestaurant.util.files;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileUtilImpl implements FileUtil {
    @Override
    public String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();

        File file = new File(filePath);
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String line;

        while ((line = bf.readLine()) != null){
            sb.append(line).append(System.lineSeparator());
        }

        return sb.toString();
    }

    @Override
    public File readFromFile(String path) {
        return new File(path);
    }

    @Override
    public File writeToFile(String path) {
        return new File(path);
    }

}
