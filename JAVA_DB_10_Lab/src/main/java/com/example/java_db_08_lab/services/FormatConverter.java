package com.example.java_db_08_lab.services;

public interface FormatConverter {

    void setPrettyPrint();

    String serialize(Object object);

    void serialize(Object obj, String fileName);


    <T> T deserialize(String format, Class<T> toType);

    <T> T deserializeFromFile(String fileName, Class<T> toType);
}
