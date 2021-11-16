package com.example.java_db_09_exercise.util.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public interface FileUtil {
    String readFileContent(String path) throws IOException;
    FileReader readFromFile(String path) throws FileNotFoundException;
    File writeToFile(String path);
}
