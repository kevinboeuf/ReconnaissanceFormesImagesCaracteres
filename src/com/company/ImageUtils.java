package com.company;

import javafx.geometry.Point2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ImageUtils {

    public static BufferedImage loadImage(String url){
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(url));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage getBlackWhiteImage(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = im.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        return im;
    }

    public static BufferedImage rotateImage(BufferedImage image, int centerX, int centerY) {
        BufferedImage im = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = im.createGraphics();
        g2d.rotate(Math.toRadians(106), centerX, centerY);
        g2d.drawImage(image, 0, 0, null);
        return im;
    }

    public static BufferedImage flipImageHorizontally(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    public static BufferedImage flipImageVertically(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    public static BufferedImage drawLine(BufferedImage image, Pixel a, Pixel b) {
        /*BufferedImage im = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = im.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setColor(Color.RED);
        g2d.drawLine(a.x, a.y, b.x, b.y);
        return im;*/

        Graphics2D g2d = image.createGraphics();

        // Draw on the buffered image
        g2d.setColor(Color.WHITE);
        //g2d.fill(new Ellipse2D.Float(0, 0, 200, 100));
        g2d.drawLine(a.x, a.y, b.x, b.y);
        g2d.dispose();
        return  image;
    }

    /**
     * displays an image
     * @param image
     * @return
     */
    public static void showBufferedImage(BufferedImage image) {
        JFrame jFrame = new JFrame();
        ImagePanel imagePanel = new ImagePanel();
        jFrame.setSize(250, 250);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.add(imagePanel);
        imagePanel.setImage(image);
        jFrame.repaint();
        jFrame.setVisible(true);
    }

    public static int[][] getImagePixels(BufferedImage image) {
        byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int[][] arrayImage = new int[image.getWidth()][image.getHeight()];
        int sum = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if(image.getRGB(x, y) == Color.WHITE.getRGB()) {
                    arrayImage[x][y] = 255;
                    sum += 1;
                } else {
                    arrayImage[x][y] = 0;
                }
            }
        }
        System.out.println(sum);
        return arrayImage;
    }

    public static Pixel getTopLeftMostCharacterPixelCoordinates(int[][] image) {
        Pixel topLeftMost = new Pixel(0, 0);
        boolean found = false;
        for(int x = 0; x < image[0].length; x++) {
            for(int y = 0; y < image.length; y++) {
                if(image[y][x] == 0) {
                    topLeftMost = new Pixel(y, x);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }
        return topLeftMost;
    }

    public static Pixel getTopRightMostCharacterPixelCoordinates(int[][] image) {
        Pixel topRightMost = new Pixel(0, 0);
        boolean found = false;
        for(int x = 0; x < image[0].length; x++) {
            for(int y = image.length - 1; y >= 0; y--) {
                if(image[y][x] == 0) {
                    topRightMost = new Pixel(y, x);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }
        return topRightMost;
    }

    public static Pixel getBottomLeftMostCharacterPixelCoordinates(int[][] image) {
        Pixel bottomLeftMost = new Pixel(0, 0);

        boolean found = false;
        for(int x = image[0].length - 1; x >= 0; x--) {
            for(int y = 0; y < image.length; y++) {
                if(image[y][x] == 0) {
                    bottomLeftMost = new Pixel(y, x);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }
        return bottomLeftMost;
    }

    public static Pixel getBottomRightMostCharacterPixelCoordinates(int[][] image) {
        Pixel bottomLeftMost = new Pixel(0, 0);

        boolean found = false;
        for(int x = image[0].length - 1; x >= 0; x--) {
            for(int y = image.length - 1; y >=0; y--) {
                if(image[y][x] == 0) {
                    bottomLeftMost = new Pixel(y, x);
                    found = true;
                    break;
                }
            }
            if(found) break;
        }
        return bottomLeftMost;
    }

    public static BufferedImage drawImageFromMatrix(int[][] matrix, int BlackColor, int WhiteColor) {
        int xLenght = matrix.length;
        int yLength = matrix[0].length;
        BufferedImage b = new BufferedImage(xLenght, yLength, BufferedImage.TYPE_BYTE_BINARY);

        for(int x = 0; x < xLenght; x++) {
            for(int y = 0; y < yLength; y++) {
                if(matrix[x][y] == WhiteColor) {
                    b.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    b.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return b;
    }




}
