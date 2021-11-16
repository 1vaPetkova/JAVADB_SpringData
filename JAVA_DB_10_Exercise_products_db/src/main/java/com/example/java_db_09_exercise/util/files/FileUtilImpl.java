package com.example.java_db_09_exercise.util.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtilImpl implements FileUtil {
    @Override
    public String readFileContent(String path) throws IOException {
        return Files.readString(Path.of(path));
    }

    @Override
    public FileReader readFromFile(String path) throws FileNotFoundException {
        return new FileReader(path);
    }

    @Override
    public File writeToFile(String path) {
        return new File(path);
    }
}
