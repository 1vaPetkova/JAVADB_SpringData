package com.example.java_db_08_lab.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Component("json_format_converter")
public class JSONFormatConverter implements FormatConverter {
    private final GsonBuilder gsonBuilder;
    private Gson gson;

    public JSONFormatConverter(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    @Override
    public void setPrettyPrint() {
        this.gsonBuilder.setPrettyPrinting();
        this.gson = null;
    }

    @Override
    public String serialize(Object object) {
        return this.getGson().toJson(object);
    }


    @Override
    public void serialize(Object object, String fileName) {
        try (FileWriter fw = new FileWriter(fileName)) {
            this.getGson().toJson(object, fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T deserialize(String input, Class<T> toType) {
        return this.getGson().fromJson(input, toType);
    }

    @Override
    public <T> T deserializeFromFile(String fileName, Class<T> toType) {
        try(FileReader fr = new FileReader(fileName)) {
            return this.getGson().fromJson(fr, toType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Gson getGson() {
        if (this.gson == null) {
            this.gson = this.gsonBuilder.create();
        }
        return this.gson;
    }
}
