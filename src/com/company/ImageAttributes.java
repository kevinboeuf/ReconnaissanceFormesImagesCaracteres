package com.company;

import com.company.ImageFormat;

/**
 * Created by Kevin on 15/01/2016.
 */
public class ImageAttributes {
    Attribute<Integer> size;
    Attribute<Integer> relevantSurface;
    Attribute<ImageFormat> format;

    public ImageAttributes(Attribute<Integer> size, Attribute<Integer> relevantSurface, Attribute<ImageFormat> format) {
        this.size = size;
        this.relevantSurface = relevantSurface;
        this.format = format;
    }


}
