package com.imagemanager.model.filter;

import javafx.scene.image.WritableImage;

public interface IFilter {
    WritableImage apply(WritableImage image);
    String getName();
}