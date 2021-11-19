package alararestaurant.util.files;

import java.io.File;

import java.io.IOException;

public interface FileUtil {

    String readFile(String filePath) throws IOException;

    File readFromFile(String path);

    File writeToFile(String path);

}
