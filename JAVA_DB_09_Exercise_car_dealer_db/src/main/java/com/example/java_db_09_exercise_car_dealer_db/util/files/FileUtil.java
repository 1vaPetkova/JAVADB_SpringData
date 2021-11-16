package com.example.java_db_09_exercise_car_dealer_db.util.files;

import java.io.IOException;

public interface FileUtil {
    String readFileContent(String path) throws IOException;
    void writeToFile(String path, String content) throws IOException;
}
