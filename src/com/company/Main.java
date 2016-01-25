package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static JFrame jframe = new JFrame();
    public static Container pane = new Container();
    public static ImagePanel imagePanel = new ImagePanel();

    public static void main(String[] args) {

        System.out.println("\n=== Récupération des images ===\n");

        System.out.println("\tDossier d'images : " + LocalConfiguration.FOLDER);

        //Get the list of images
        List<ImageRelation> imageRelationAttributesList = new ArrayList<ImageRelation>();
        HashMap <String, Image> imagesList = getImagesList(1, 2);

        System.out.println("\t" + imagesList.size() + " images récupérées");

        //Show the images
        initDrawingFrame();
        for (Map.Entry<String, Image> entry : imagesList.entrySet()){
            BufferedImage grayscale = toGray(getGaussianBluredImage(entry.getValue().bufferedImage));
            BufferedImage binarized = binarize(entry.getKey(), grayscale);
        }

        BufferedImage image;

        System.out.println("\n=== Extraction des attributs des images ===\n");

        //Extract the data
        for (Map.Entry<String, Image> entry : imagesList.entrySet()){
            image = entry.getValue().bufferedImage;
            imageRelationAttributesList.add(getImageRelation(image, entry.getValue().imageClass));
        }

        //Generate the ARFF
        ImageRelation.generateARFF(imageRelationAttributesList);

        System.out.println("\tDone");
    }

    public static ImageRelation getImageRelation(BufferedImage image, ImageClass imageClass){
        int size = getSize(image);
        FormatAttribute format = getFormat(image);
        ImageRelation.Builder builder = new ImageRelation.Builder();
        return builder.setSize(size)
                .setFormat(format)
                .setClasse(imageClass)
                .build();
    }

    /**
     * Get the size of an image
     * @param image
     * @return
     */
    public static int getSize(BufferedImage image){
        return image.getWidth()*image.getHeight();
    }

    /**
     * Get the format of the image
     * @param image
     * @return
     */
    public static FormatAttribute getFormat(BufferedImage image){
        int height = image.getHeight();
        int width = image.getWidth();
        FormatAttribute format = FormatAttribute.SQUARE;
        if(height > width){
            format = FormatAttribute.VERTICAL_RECTANGLE;
        }else if (width > height){
            format = FormatAttribute.HORIZONTAL_RECTANGLE;
        }
        return format;
    }

    /**
     * Reads all the images from the constant folder and returns it as a list of BufferedImages
     * @param beginningIndex
     * @param endingIndex
     * @return
     */
    public static HashMap<String, Image> getImagesList(int beginningIndex, int endingIndex){
        HashMap <String, Image> imagesList = new HashMap <String, Image>();
        BufferedImage img = null;
        List<String> results = null;

        for (int i = beginningIndex; i <= endingIndex; i++) {
            File[] files = new File(LocalConfiguration.FOLDER + "Sample" + String.format("%03d", i)).listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        img = ImageIO.read(file);
                        ImageClass imageClass = ImageClass.getImageClass("Sample" + String.format("%03d", i));
                        imagesList.put(file.getName(), new Image(file.getName(), img, imageClass));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return imagesList;
    }

    /**
     * displays an image
     * @param image
     * @return
     */
    public static void showBufferedImage(String title, BufferedImage image) {
        jframe.setTitle(title);
        imagePanel.setImage(image);
        imagePanel.repaint();
    }

    /**
     * Initialize the drawing frame
     */
    public static void initDrawingFrame(){
        jframe.setSize(250, 250);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        imagePanel = new ImagePanel();
        pane = jframe.getContentPane();
        pane.add(imagePanel);
        jframe.setVisible(true);
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
     * Returns the histogram of the image
     */
   public static int[] imageHistogram(BufferedImage input) {

        int[] histogram = new int[256];

        for(int i=0; i<histogram.length; i++)
            histogram[i] = 0;

        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
        return histogram;
    }

    /**
     * Turns the image to a gray scaled one
     * @param original
     * @return
     */
    private static BufferedImage toGray(BufferedImage original) {

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
     * Get the threshold used to turn the image to black and white
     * @param original
     * @return
     */
    private static int getThreshold(BufferedImage original) {

        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;

            if(wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    /**
     * Turn the image to black and white only
     * @param name
     * @param original
     * @return
     */
    private static BufferedImage binarize(String name, BufferedImage original) {
        int red;
        int newPixel;
        int threshold = getThreshold(original);
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {

                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);
            }
        }

        showBufferedImage(name, binarized);
        return binarized;
    }

    /**
     * Convert R, G, B, Alpha to standard 8 bit
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }
}
