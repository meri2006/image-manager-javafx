package com.imagemanager.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.util.ArrayList;
import java.util.List;

public class ImageModel {

    private String filePath;
    private String fileName;
    private WritableImage currentImage;
    private WritableImage originalImage;
    private List<String> tags;
    private List<String> appliedTransformations;

    public ImageModel(String filePath, Image image) {
        this.filePath = filePath;
        this.fileName = extractFileName(filePath);
        this.tags = new ArrayList<>();
        this.appliedTransformations = new ArrayList<>();
        this.originalImage = toWritableImage(image);
        this.currentImage = toWritableImage(image);
    }

    public ImageModel() {
        this.tags = new ArrayList<>();
        this.appliedTransformations = new ArrayList<>();
    }

    private String extractFileName(String path) {
        if (path == null) return "";
        int idx = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return path.substring(idx + 1);
    }

    public static WritableImage toWritableImage(Image image) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        WritableImage wi = new WritableImage(w, h);
        PixelWriter pw = wi.getPixelWriter();
        PixelReader pr = image.getPixelReader();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                pw.setArgb(x, y, pr.getArgb(x, y));
        return wi;
    }

    public void resetToOriginal() {
        this.currentImage = toWritableImage(originalImage);
        this.appliedTransformations.clear();
    }

    public void addTag(String tag) {
        if (tag != null && !tag.isBlank() && !tags.contains(tag))
            tags.add(tag.trim().toLowerCase());
    }

    public void removeTag(String tag) { tags.remove(tag); }

    public boolean hasTag(String tag) {
        return tags.contains(tag.trim().toLowerCase());
    }

    public void addTransformation(String name) { appliedTransformations.add(name); }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public WritableImage getCurrentImage() { return currentImage; }
    public void setCurrentImage(WritableImage currentImage) { this.currentImage = currentImage; }
    public WritableImage getOriginalImage() { return originalImage; }
    public void setOriginalImage(WritableImage originalImage) { this.originalImage = originalImage; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public List<String> getAppliedTransformations() { return appliedTransformations; }
    public void setAppliedTransformations(List<String> t) { this.appliedTransformations = t; }
}