package com.imagemanager.model.filter;

public class SwapRGBFilter extends AbstractPixelFilter {

    @Override
    protected int transformPixel(int argb) {
        int a = alpha(argb);
        int r = red(argb);
        int g = green(argb);
        int b = blue(argb);
        return toArgb(a, g, b, r);
    }

    @Override
    public String getName() { return "SwapRGB"; }
}