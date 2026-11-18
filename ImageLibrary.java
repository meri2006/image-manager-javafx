package com.imagemanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageLibrary {

    private List<ImageModel> images;

    public ImageLibrary() {
        this.images = new ArrayList<>();
    }

    public void addImage(ImageModel image) {
        images.add(image);
    }

    public void removeImage(ImageModel image) {
        images.remove(image);
    }

    public List<ImageModel> searchByTag(String tag) {
        return images.stream()
                .filter(img -> img.hasTag(tag))
                .collect(Collectors.toList());
    }

    public List<ImageModel> searchByName(String name) {
        String lower = name.toLowerCase();
        return images.stream()
                .filter(img -> img.getFileName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<ImageModel> getImages() { return images; }
    public void setImages(List<ImageModel> images) { this.images = images; }
    public int size() { return images.size(); }
}