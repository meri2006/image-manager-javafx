package com.imagemanager.model.filter;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class PrewittFilter implements IFilter {

    private static final int[][] KERNEL_X = {
            {-1, 0, 1},
            {-1, 0, 1},
            {-1, 0, 1}
    };

    private static final int[][] KERNEL_Y = {
            {-1, -1, -1},
            { 0,  0,  0},
            { 1,  1,  1}
    };

    @Override
    public WritableImage apply(WritableImage image) {
        int width  = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage gray = new GrayscaleFilter().apply(image);
        WritableImage result = new WritableImage(width, height);
        PixelReader reader = gray.getPixelReader();
        PixelWriter writer = result.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gx = 0, gy = 0;
                for (int ky = -1; ky <= 1; ky++) {   // car ona la valeur min cest -1 et max cest 1 ca cest de matrice
                    for (int kx = -1; kx <= 1; kx++) {
                        int px = clamp(x + kx, 0, width - 1);//

                        int py = clamp(y + ky, 0, height - 1);
                        int argb = reader.getArgb(px, py);
                        int grayVal = (argb >> 16) & 0xFF;  // //On lit le pixel voisin
                        gx += grayVal * KERNEL_X[ky + 1][kx + 1];// multiplie la valeur de chaque voisin par le coefficient du noyau
                        gy += grayVal * KERNEL_Y[ky + 1][kx + 1];
                    }
                }
                int magnitude = clampColor((int) Math.sqrt(gx * gx + gy * gy));
                int argbResult = (255 << 24) | (magnitude << 16) | (magnitude << 8) | magnitude;
                writer.setArgb(x, y, argbResult);
            }
        }
        return result;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private int clampColor(int value) {
        return Math.max(0, Math.min(255, value));
    }

    @Override
    public String getName() { return "Prewitt"; }
}