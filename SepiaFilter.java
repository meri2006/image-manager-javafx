package com.imagemanager.model.filter;

public class SepiaFilter extends AbstractPixelFilter {

    @Override
    protected int transformPixel(int argb) {
        int a = alpha(argb);
        int r = red(argb);
        int g = green(argb);
        int b = blue(argb);

        int newR = clamp((int)(r * 0.393 + g * 0.769 + b * 0.189));
        int newG = clamp((int)(r * 0.349 + g * 0.686 + b * 0.168));
        int newB = clamp((int)(r * 0.272 + g * 0.534 + b * 0.131));

        return toArgb(a, newR, newG, newB);
    }

    @Override
    public String getName() { return "Sepia"; }
}