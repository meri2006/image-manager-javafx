package com.imagemanager.model.transform;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class FlipTransformation implements ITransformation {

    public enum FlipDirection {
        HORIZONTAL, VERTICAL
    }

    private final FlipDirection direction;

    public FlipTransformation(FlipDirection direction) {
        this.direction = direction;
    }

    @Override
    public WritableImage apply(WritableImage image) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        WritableImage result = new WritableImage(w, h);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = result.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (direction == FlipDirection.HORIZONTAL) {
                    writer.setArgb(w - 1 - x, y, reader.getArgb(x, y));
                } else {
                    writer.setArgb(x, h - 1 - y, reader.getArgb(x, y));
                }
            }
        }
        return result;
    }

    @Override
    public String getName() { return "Flip" + direction.name(); }
}