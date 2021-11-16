package com.example.java_db_09_exercise_car_dealer_db.util.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class FileUtilImpl implements FileUtil {
    @Override
    public String readFileContent(String path) throws IOException {
        return Files.readString(Path.of(path));
    }

    @Override
    public void writeToFile(String path, String content) throws IOException {
        Files.write(Path.of(path), Collections.singleton(content));
    }
}
