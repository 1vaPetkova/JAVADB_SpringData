package com.example.java_db_06_exercise.util;

import java.io.IOException;
import java.util.List;

public interface FileUtil {

    List<String> getFileContent(String filePath) throws IOException;
}
