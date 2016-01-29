package com.company.utils;

import com.company.model.*;
import com.company.model.Image;
import com.company.model.SDDImage;
import com.company.model.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    public static BufferedImage loadImage(File file){
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    public static <T> void showImageList(ArrayList<? extends Image> imagesList, int width, int height) {
        JFrame jframe = new JFrame();
        JPanel jPanel = new JPanel();
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jframe.add(jScrollPane);
        jPanel.setLayout(new FlowLayout());
        jframe.setSize(width, height);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        for(Image image : imagesList) {
            jPanel.add(new JLabel(new ImageIcon(image.bufferedImage)));
        }
    }

    public static void showBufferedImagesList(ArrayList<BufferedImage> imageList, int width, int height) {
        JFrame jframe = new JFrame();
        JPanel jPanel = new JPanel();
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jframe.add(jScrollPane);
        jPanel.setLayout(new FlowLayout());
        jframe.setSize(width, height);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        for(BufferedImage image : imageList) {
            jPanel.add(new JLabel(new ImageIcon(image)));
        }
    }

    /**
     * Return an image with an applied Gaussian Blur
     * @param image
     * @return
     */
    public static BufferedImage getGaussianBluredImage(BufferedImage image){
        float[] matrix = {
                1/16f, 1/8f, 1/16f,
                1/8f, 1/4f, 1/8f,
                1/16f, 1/8f, 1/16f,
        };

        BufferedImage original = image;
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix), ConvolveOp.EDGE_NO_OP, null);
        BufferedImage blurredImage = op.filter(original, null);

        return blurredImage;
    }

    /**
     * Turns the image to a gray scaled one
     * @param original
     * @return
     */
    public static BufferedImage getGrayScaleImage(BufferedImage original) {

        int alpha, red, green, blue;
        int newPixel;

        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);

                // Write pixels into image
                lum.setRGB(i, j, newPixel);

            }
        }
        return lum;
    }

    /**
     * Convert R, G, B, Alpha to standard 8 bit
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }






    /**
     * Apply a mask to an image and return the result
     * @param image
     * @param mask
     * @return
     */
    public static BufferedImage getMaskedImagehein(BufferedImage image, final BufferedImage mask, int maskDeleteColor, int maskNewColor){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage res = image;

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if(res.getRGB(i, j) == maskDeleteColor){
                    res.setRGB(i, j, maskNewColor);
                }
            }
        }
        return res;
    }


    //--

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
}
