package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int beginningIndex = 1;
    public static final int endingIndex = 62;

    public static JFrame jframe = new JFrame();
    public static JScrollPane jScrollPane = new JScrollPane();
    public static JPanel jPanel = new JPanel();

    public static void main(String[] args) {

        System.out.println("\n=== Récupération des images ===\n");

        System.out.println("\tDossier d'images : " + LocalConfiguration.FOLDER);

        //Get the list of images
        List<ImageRelation> imageRelationAttributesList = new ArrayList<ImageRelation>();
        ArrayList<Image> imagesList = getImagesList(beginningIndex, endingIndex);
        ArrayList<BufferedImage> masksList = getMasksList(beginningIndex, endingIndex);
        ArrayList<BufferedImage> binarizedImageList = new ArrayList<BufferedImage>();

        System.out.println("\t" + imagesList.size() + " images récupérées");


        //Show the images
        initDrawingFrame();
        for (int i=0; i <imagesList.size(); i++){
            BufferedImage grayscale = toGray(getGaussianBluredImage(imagesList.get(i).bufferedImage));
            BufferedImage maskNormalized = null;
            BufferedImage normalized = null;
            try {
                normalized = getScaledImage(grayscale, 128, 128);
                maskNormalized = getScaledImage(masksList.get(i), 128, 128);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedImage binarized = binarize(normalized);
            if(isBackgroundWhite(binarized)){
                binarized = invertImageColors(binarized);
            }
            BufferedImage maskedBinarized = applyMask(binarized, maskNormalized);
            binarizedImageList.add(maskedBinarized);
            showRepartition(maskedBinarized);
            showBufferedImage(maskedBinarized);
        }

        BufferedImage bufferedImage;

        System.out.println("\n=== Extraction des attributs des images ===\n");

        //Extract the data
        for (Image image : imagesList){
            bufferedImage = image.bufferedImage;
            imageRelationAttributesList.add(getImageRelation(bufferedImage, image.imageClass));
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
    public static ArrayList<Image> getImagesList(int beginningIndex, int endingIndex){
        ArrayList<Image> imagesList = new ArrayList<Image>();
        BufferedImage img = null;

        for (int i = beginningIndex; i <= endingIndex; i++) {
            File[] files = new File(LocalConfiguration.FOLDER + "Sample" + String.format("%03d", i)).listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        img = ImageIO.read(file);
                        ImageClass imageClass = ImageClass.getImageClass("Sample" + String.format("%03d", i));
                        imagesList.add(new Image(file.getName(), img, imageClass));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return imagesList;
    }

    /**
     * Reads all the images from the constant folder and returns it as a list of BufferedImages
     * @param beginningIndex
     * @param endingIndex
     * @return
     */
    public static ArrayList<BufferedImage> getMasksList(int beginningIndex, int endingIndex){
        ArrayList<BufferedImage> imagesList = new ArrayList<BufferedImage>();
        BufferedImage img = null;

        for (int i = beginningIndex; i <= endingIndex; i++) {
            File[] files = new File(LocalConfiguration.MASKFOLDER + "Sample" + String.format("%03d", i)).listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        img = ImageIO.read(file);
                        imagesList.add(img);
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
    public static void showBufferedImage(BufferedImage image) {
        jPanel.add(new JLabel(new ImageIcon(image)));
    }

    /**
     * Initialize the drawing frame
     */
    public static void initDrawingFrame(){
        jScrollPane = new JScrollPane(jPanel);
        jframe.add(jScrollPane);
        jPanel.setLayout(new FlowLayout());

        jframe.setSize(250, 250);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
     * @param original
     * @return
     */
    private static BufferedImage binarize(BufferedImage original) {
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

    /**
     * Return true if the background is white for the binarized image given as a parameter
     * @param image
     * @return
     */
    public static boolean isBackgroundWhite(BufferedImage image) {

        boolean res = false;
        int blackPixelCount = 0;
        int whitePixelCount = 0;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        for(int i=0; i<imageWidth; i++){
            if(new Color(image.getRGB(i, 0)).getRed() == 255){
                whitePixelCount ++;
            }else if (new Color(image.getRGB(i, 0)).getRed() == 0){
                blackPixelCount ++;
            }

            if(new Color(image.getRGB(i, imageHeight-1)).getRed() == 255){
                whitePixelCount ++;
            }else if (new Color(image.getRGB(i, imageHeight-1)).getRed() == 0){
                blackPixelCount ++;
            }
        }

        for(int i=0; i<imageHeight; i++){
            if(new Color(image.getRGB(0, i)).getRed() == 255){
                whitePixelCount ++;
            }else if (new Color(image.getRGB(0, i)).getRed() == 0){
                blackPixelCount ++;
            }

            if(new Color(image.getRGB(imageWidth-1, i)).getRed() == 255){
                whitePixelCount ++;
            }else if (new Color(image.getRGB(imageWidth-1, i)).getRed() == 0){
                blackPixelCount ++;
            }
        }

        if (whitePixelCount >= blackPixelCount){
            res = true;
        }
        return res;
    }

    /**
     * Invert the colors or the binarized image
     * @param image
     * @return
     */
    public static BufferedImage invertImageColors(BufferedImage image){
        BufferedImage result = image;
        int color, newPixel = 255;
        for (int i=0; i<image.getWidth(); i++){
            for (int j=0; j<image.getHeight(); j++){
                color = new Color(image.getRGB(i, j)).getRed();
                int alpha = new Color(image.getRGB(i, j)).getAlpha();
                if(color == 255){
                    newPixel=0;
                }else if(color == 0){
                    newPixel=255;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                result.setRGB(i, j, newPixel);
            }
        }
        return result;
    }

    /**
     * Return a scaled image
     * @param image
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }

    /**
     * Apply a mask to an image and return the result
     * @param image
     * @param mask
     * @return
     */
    public static BufferedImage applyMask(BufferedImage image, BufferedImage mask){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage res = image;
        BufferedImage appliedMask = mask;
        int imageColor;
        int maskColor;

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                imageColor = new Color(res.getRGB(i, j)).getRed();
                maskColor = new Color(appliedMask.getRGB(i, j)).getRed();
                if(!(imageColor == 255 && maskColor == 255)){
                    res.setRGB(i, j, 0);
                }
            }
        }
        return res;
    }

    public static void showRepartition (BufferedImage binarized){
        int width = binarized.getWidth();
        int height = binarized.getHeight();
        int topLeftWhitePixelCount = 0;
        int topRightWhitePixelCount = 0;
        int bottomLeftWhitePixelCount = 0;
        int bottomRightWhitePixelCount = 0;

        for (int i=0; i<(width/2); i++){
            for (int j=0; j<(height/2); j++){
                if(new Color(binarized.getRGB(i, j)).getRed() == 255){
                    topLeftWhitePixelCount++;
                }
            }
            for (int k=(height/2); k<height; k++){
                if(new Color(binarized.getRGB(i, k)).getRed() == 255) {
                    bottomLeftWhitePixelCount++;
                }
            }
        }

        for (int i=(width/2); i<width; i++){
            for (int j=0; j<(height/2); j++){
                if(new Color(binarized.getRGB(i, j)).getRed() == 255) {
                    topRightWhitePixelCount++;
                }
            }
            for (int k=(height/2); k<height; k++){
                if(new Color(binarized.getRGB(i, k)).getRed() == 255){
                    bottomRightWhitePixelCount++;
                }
            }
        }

        System.out.println("Top left : " + topLeftWhitePixelCount + ", Top right : " + topRightWhitePixelCount + ", bottom left : " + bottomLeftWhitePixelCount + ", bottom right : " + bottomRightWhitePixelCount);
    }

}
