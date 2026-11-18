package com.imagemanager.model.filter;

public class GrayscaleFilter extends AbstractPixelFilter {

    @Override
    protected int transformPixel(int argb) {
        int a = alpha(argb);
        int r = red(argb);
        int g = green(argb);
        int b = blue(argb);
        int avg = (r + g + b) / 3;
        return toArgb(a, avg, avg, avg);
    }

    @Override
    public String getName() { return "Grayscale"; }
}