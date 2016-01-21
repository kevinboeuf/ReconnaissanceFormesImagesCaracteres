package com.company;

import java.awt.image.BufferedImage;

public class Image {
    public String name ="";
    public BufferedImage bufferedImage;
    public ImageClass imageClass = ImageClass.ZERO;

    public Image(String name, BufferedImage bufferedImage, ImageClass imageClass) {
        this.name = name;
        this.bufferedImage = bufferedImage;
        this.imageClass = imageClass;
    }
}
