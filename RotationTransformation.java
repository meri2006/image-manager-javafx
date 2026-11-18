package com.imagemanager.model.transform;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class RotationTransformation implements ITransformation {

    private final int degrees;

    public RotationTransformation(int degrees) {
        if (degrees != 90 && degrees != 180 && degrees != 270)
            throw new IllegalArgumentException("La rotation doit être 90, 180 ou 270 degrés.");
        this.degrees = degrees;
    }

    @Override
    public WritableImage apply(WritableImage image) {
        WritableImage result = image;
        int times = degrees / 90;
        for (int i = 0; i < times; i++)
            result = rotate90(result);
        return result;
    }

    private WritableImage rotate90(WritableImage src) {
        int w = (int) src.getWidth();
        int h = (int) src.getHeight();
        WritableImage dst = new WritableImage(h, w);
        PixelReader reader = src.getPixelReader();
        PixelWriter writer = dst.getPixelWriter();

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                writer.setArgb(h - 1 - y, x, reader.getArgb(x, y));
        return dst;
    }

    @Override
    public String getName() { return "Rotation" + degrees; }
}