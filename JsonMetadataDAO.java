package com.imagemanager.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.imagemanager.model.ImageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonMetadataDAO implements IMetadataDAO {

    private static final String DEFAULT_PATH = "metadata.json";
    private final String filePath;
    private final ObjectMapper mapper;// la classe principale de Jackson qui fait la conversion Java ↔ JSON

    public JsonMetadataDAO() {
        this(DEFAULT_PATH);
    }

    public JsonMetadataDAO(String filePath) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    // sauvegarder tous les images
    @Override
    public void saveAll(List<ImageModel> images) {
        List<MetadataDTO> dtos = new ArrayList<>();
        for (ImageModel img : images)
            dtos.add(toDTO(img));
        try {
            mapper.writeValue(new File(filePath), dtos);
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde JSON : " + e.getMessage());
        }
    }

    @Override
    public List<MetadataDTO> loadAll() {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();
        try {
            MetadataDTO[] dtos = mapper.readValue(file, MetadataDTO[].class);
            return new ArrayList<>(Arrays.asList(dtos));
        } catch (IOException e) {
            System.err.println("Erreur chargement JSON : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(ImageModel image) {
        List<MetadataDTO> existing = loadAll();
        boolean found = false;
        for (int i = 0; i < existing.size(); i++) {
            if (existing.get(i).getFilePath().equals(image.getFilePath())) {
                existing.set(i, toDTO(image));
                found = true;
                break;
            }
        }
        if (!found) existing.add(toDTO(image));
        try {
            mapper.writeValue(new File(filePath), existing);
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde JSON : " + e.getMessage());
        }
    }

    private MetadataDTO toDTO(ImageModel image) {
        return new MetadataDTO(
                image.getFilePath(),
                image.getFileName(),
                new ArrayList<>(image.getTags()),
                new ArrayList<>(image.getAppliedTransformations())
        );
    }
}
