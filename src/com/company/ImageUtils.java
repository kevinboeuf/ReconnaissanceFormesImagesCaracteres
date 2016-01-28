package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

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

        for (int x = 0; x < image.getHeight(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
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

    public static Rectangle getMatrixBoundaries(int[][] matrix, int characterColor) {
        int height = matrix.length;
        int width = matrix[0].length;
        Rectangle rectangle = new Rectangle();
        rectangle.top = new Pixel(0, height);
        rectangle.left = new Pixel(width, 0);
        rectangle.bottom = new Pixel(0, 0);
        rectangle.right = new Pixel(0, 0);

        for(int x = 0; x < matrix[0].length; x++) {
            for(int y = 0; y < matrix.length; y++) {
                if(matrix[x][y] == characterColor) {
                    if(y < rectangle.top.y) {
                        rectangle.top = new Pixel(x, y);
                    }
                    if(x < rectangle.left.x) {
                        rectangle.left = new Pixel(x, y);
                    }
                    if(y > rectangle.bottom.y) {
                        rectangle.bottom = new Pixel(x, y);
                    }
                    if(x > rectangle.right.x) {
                        rectangle.right = new Pixel(x, y);
                    }
                }
            }
        }
        return rectangle;
    }

    public static BufferedImage drawImageFromMatrix(int[][] matrix, int BlackColor, int WhiteColor) {
        int yLenght = matrix.length;
        int xLength = matrix[0].length;
        BufferedImage b = new BufferedImage(xLength, yLenght, BufferedImage.TYPE_BYTE_BINARY);

        for(int x = 0; x < xLength; x++) {
            for(int y = 0; y < yLenght; y++) {
                if(matrix[y][x] == WhiteColor) {
                    b.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    b.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return b;
    }

    public static BufferedImage cropImage(BufferedImage image, Rectangle rect) {
        BufferedImage dest = image.getSubimage(rect.left.x, rect.top.y, rect.getWidth(), rect.getHeight());
        return dest;
    }



}
