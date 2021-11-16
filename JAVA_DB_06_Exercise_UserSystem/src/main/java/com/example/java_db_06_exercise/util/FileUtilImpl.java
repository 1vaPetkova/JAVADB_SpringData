package com.example.java_db_06_exercise.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtilImpl implements FileUtil {

    @Override
    public List<String> getFileContent(String filePath) throws IOException {

        return Files.readAllLines(Path.of(filePath))
                .stream().filter(l -> !l.isEmpty()).collect(Collectors.toList());
    }
}
