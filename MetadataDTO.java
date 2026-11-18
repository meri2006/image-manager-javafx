package com.imagemanager.dao;

import java.util.List;
//Sauvegarder les informations d'une image dans un fichier JSON sans les pixels
public class MetadataDTO {


        private String filePath;        // chemin du fichier
        private String fileName;        // nom du fichier
        private List<String> tags;      // les tags
        private List<String> transformations; // historique


    public MetadataDTO() {}

    public MetadataDTO(String filePath, String fileName,
                       List<String> tags, List<String> transformations) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.tags = tags;
        this.transformations = transformations;
    }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getTransformations() { return transformations; }
    public void setTransformations(List<String> transformations) {
        this.transformations = transformations;
    }
}