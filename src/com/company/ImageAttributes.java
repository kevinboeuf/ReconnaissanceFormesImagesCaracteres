package com.company;

import com.company.ImageFormat;

/**
 * Created by Kevin on 15/01/2016.
 */
public class ImageAttributes {
    int size;
    int relevantSurface;
    ImageFormat format;

    public ImageAttributes(int size, int relevantSurface, ImageFormat format) {
        this.size = size;
        this.relevantSurface = relevantSurface;
        this.format = format;
    }
}
