package com.imagemanager.model.filter;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public abstract class AbstractPixelFilter implements IFilter {

    @Override
    public WritableImage apply(WritableImage image) {
        int width  = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage result = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = result.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y); //lit les pixel a la position x , y
                int transformed = transformPixel(argb);
                writer.setArgb(x, y, transformed);
            }
        }
        return result;
    }

    protected abstract int transformPixel(int argb);
    // pour qu'on sort les quantités de l'original
    protected int alpha(int argb) {
        return (argb >> 24) & 0xFF;
    }
    protected int red(int argb)   {
        return (argb >> 16) & 0xFF;
    }
    protected int green(int argb) {
        return (argb >>  8) & 0xFF; }
    protected int blue(int argb)  {
        return  argb        & 0xFF; }
    //On met la nouvelle couleur dans le pixel
    protected int toArgb(int a, int r, int g, int b) {
        return (clamp(a) << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
    }
//0 a 255
    protected int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}