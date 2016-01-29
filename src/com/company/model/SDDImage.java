package com.company.model;

import java.awt.image.BufferedImage;

public class SDDImage implements Cloneable {
    public String name ="";
    public Image image;
    public ImageClass imageClass = ImageClass.ZERO;
    public ImageRelation.Builder builder = new ImageRelation.Builder();

    public SDDImage(String name, Image image, ImageClass imageClass) {
        this.name = name;
        this.image = image;
        this.imageClass = imageClass;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new SDDImage(name, (Image)image.clone(), imageClass);
    }
}
