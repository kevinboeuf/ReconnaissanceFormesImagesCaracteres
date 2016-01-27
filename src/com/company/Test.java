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

    public static final String IMAGE_PATH = "D://English/z2.png";

    public static BufferedImage testImage;

    public static void main(String[] args) {
        /*BufferedImage testImage = ImageUtils.loadImage(IMAGE_PATH);
        testImage = ImageUtils.getBlackWhiteImage(60, testImage);
        //ImageUtils.toBlackAndWhite(testImage, 200);
        ImageUtils.showBufferedImage(testImage);

       ImageUtils.getImagePixels(testImage);
       int a = 12;*/
/*
        int[][] image = imageSmall;

        Point topLeftMost = new Point(0, 0);

        boolean found = false;
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {
                if(image[x][y] == 1) {
                    topLeftMost.setLocation(x, y);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }

        Point topRightMost = new Point(0, 0);

        boolean found2 = false;
        for(int x = 0; x < 10; x++) {
            for(int y = 9; y > 0; y--) {
                if(image[x][y] == 1) {
                    topRightMost.setLocation(x, y);
                    found2 = true;
                    break;
                }
            }
            if(found2) break;
        }

        Point bottomLeftMost = new Point(0, 0);

        boolean found3 = false;
        for(int x = 9; x > 0; x--) {
            for(int y = 0; y < 10; y++) {
                if(image[x][y] == 1) {
                    bottomLeftMost.setLocation(x, y);
                    found3 = true;
                    break;
                }
            }
            if(found3) break;
        }

        Point bottomRightMost = new Point(0, 0);

        boolean found4 = false;
        for(int x = 9; x > 0; x--) {
            for(int y = 9; y > 0; y--) {
                if(image[x][y] == 1) {
                    bottomRightMost.setLocation(x, y);
                    found4 = true;
                    break;
                }
            }
            if(found4) break;
        }


        System.out.println(topLeftMost.toString());
        System.out.println(topRightMost.toString());
        System.out.println(bottomLeftMost.toString());
        System.out.println(bottomRightMost.toString());

*/

        BufferedImage aa = ImageUtils.drawImageFromMatrix(imageSmall, 1, 0);

        //ImageUtils.showBufferedImage(aa);


        testImage = ImageUtils.loadImage(IMAGE_PATH);

        testImage = ImageUtils.getBlackWhiteImage(testImage);

        //testImage = ImageUtils.rotateImage(testImage);

        int[][] matrix = ImageUtils.getImagePixels(testImage);

        Pixel a = ImageUtils.getTopLeftMostCharacterPixelCoordinates(matrix);
        Pixel b = ImageUtils.getTopRightMostCharacterPixelCoordinates(matrix);
        Pixel c = ImageUtils.getBottomLeftMostCharacterPixelCoordinates(matrix);
        Pixel d = ImageUtils.getBottomRightMostCharacterPixelCoordinates(matrix);


        //System.out.println(a.toString());
        //System.out.println(b.toString());
        //System.out.println(c.toString());
        //System.out.println(d.toString());

        Pixel topMidPixel = a.midPixel(b);
        Pixel bottomMidPixel = c.midPixel(d);

        System.out.println(topMidPixel.toString());
        System.out.println(bottomMidPixel.toString());


        Pixel midPixel = topMidPixel.midPixel(bottomMidPixel);

        System.out.println(midPixel.toString());

        ImageUtils.drawLine(testImage, topMidPixel, bottomMidPixel);

        ImageUtils.showBufferedImage(testImage);

        BufferedImage testImageResult = ImageUtils.rotateImage(testImage, midPixel.x, midPixel.y);
        ImageUtils.showBufferedImage(testImageResult);

        BufferedImage testImageResult2 = ImageUtils.flipImageVertically(testImageResult);
        ImageUtils.showBufferedImage(testImageResult2);




        //ImageUtils.showBufferedImage(testImage);




/*
        testImage = ImageUtils.rotateImage(testImage, midPixel.x, midPixel.y);

       matrix = ImageUtils.getImagePixels(testImage);

         a = ImageUtils.getTopLeftMostCharacterPixelCoordinates(matrix);
         b = ImageUtils.getTopRightMostCharacterPixelCoordinates(matrix);
         c = ImageUtils.getBottomLeftMostCharacterPixelCoordinates(matrix);
         d = ImageUtils.getBottomRightMostCharacterPixelCoordinates(matrix);


        System.out.println(a.toString());
        System.out.println(b.toString());
        System.out.println(c.toString());
        System.out.println(d.toString());

        System.out.println(testImage.getHeight());
        System.out.println(testImage.getWidth());

        topMidPixel = a.midPixel(b);
        bottomMidPixel = c.midPixel(d);
        midPixel = topMidPixel.midPixel(bottomMidPixel);

        ImageUtils.drawLine(testImage, a, d);

*/
        //ImageUtils.showBufferedImage(aa);

    }
}
