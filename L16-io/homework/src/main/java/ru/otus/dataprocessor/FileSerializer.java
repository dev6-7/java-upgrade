package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class FileSerializer implements Serializer {
    private String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        ObjectMapper mapper = new ObjectMapper();

        var file = new File(fileName);
        try {
            mapper.writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Data saved to the file:" + file.getAbsolutePath());


    }
}
