package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Test {

    public static JFrame jframe = new JFrame();
    public static Container pane = new Container();
    public static ImagePanel imagePanel = new ImagePanel();

    public static final int[][] imageSmall = {
            {0, 0, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 0}
    };

    public static final String IMAGE_PATH = "C://Users/Gosp-enssat/Desktop/English/testA.png";

    public static BufferedImage testImage;

    public static void main(String[] args) {
        BufferedImage testImage = ImageUtils.loadImage(IMAGE_PATH);
        ImageUtils.showBufferedImage(testImage);
        int[][] matrix = ImageUtils.getImagePixels(testImage);

        Rectangle rectangle = ImageUtils.getMatrixBoundaries(matrix, 255);

        System.out.println(rectangle.top.toString());
        System.out.println(rectangle.left.toString());
        System.out.println(rectangle.bottom.toString());
        System.out.println(rectangle.right.toString());

        BufferedImage a = ImageUtils.cropImage(testImage, rectangle);

        ImageUtils.showBufferedImage(a);

    }
}
