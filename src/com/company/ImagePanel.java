package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Kevin on 19/01/2016.
 */
public class ImagePanel extends JPanel{
    BufferedImage image;

    public void ImagePanel(BufferedImage image){
        this.image = image;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, null, 50,50);
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }
}
