package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileLoader implements Loader {

    private String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        Measurement[] measurements = null;
        try (BufferedReader reader = new BufferedReader(
                new FileReader(this.getClass().getClassLoader().getResource(fileName).getFile()))
        ) {
            Gson gson = new Gson();
            measurements = gson.fromJson(reader, Measurement[].class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return !Objects.isNull(measurements) ? Arrays.asList(measurements) : List.of();
    }
}
