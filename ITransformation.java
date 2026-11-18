package com.imagemanager.model.transform;

import javafx.scene.image.WritableImage;

public interface ITransformation {
    WritableImage apply(WritableImage image);
    String getName();
}