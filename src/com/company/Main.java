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
        HashMap <String, Image> imagesList = getImagesList(1, 3);

        System.out.println("\t" + imagesList.size() + " images récupérées");

        //Show the images
        initDrawingFrame();
        for (Map.Entry<String, Image> entry : imagesList.entrySet()){
            //showBufferedImage(entry.getKey(), entry.getValue().bufferedImage);
            //getBlackWhiteImage(entry.getKey(), entry.getValue().bufferedImage);
            //getGaussianBluredImage(entry.getKey(), entry.getValue().bufferedImage);
            getBlackWhiteImage(entry.getKey(), getGaussianBluredImage(entry.getKey(), entry.getValue().bufferedImage));
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

    public static int getSize(BufferedImage image){
        return image.getWidth()*image.getHeight();
    }

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

    /*public static void getBlackWhiteImage(String name, BufferedImage image) {
        BufferedImage grayScale = image;
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        BufferedImage res = op.filter(grayScale, grayScale);

        BufferedImage blackWhite = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        showBufferedImage(name, res);
    }*/

    public static BufferedImage getBlackWhiteImage(String name, BufferedImage image){
        BufferedImage original = image;
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        try
        {

            int red;
            int newPixel;
            int threshold = 100;

            for(int i=0; i<original.getWidth(); i++)
            {
                for(int j=0; j<original.getHeight(); j++)
                {
                    // Get pixels
                    red = new Color(original.getRGB(i, j)).getGreen();
                    int alpha = new Color(original.getRGB(i, j)).getAlpha();
                    if(red > threshold)
                    {
                        newPixel = 0;
                    }
                    else
                    {
                        newPixel = 255;
                    }
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    binarized.setRGB(i, j, newPixel);
                }
            }
            ImageIO.write(binarized, "png",new File("blackwhiteimage") );
            showBufferedImage(name, binarized);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return binarized;
    }

    public static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }

    public static BufferedImage getGaussianBluredImage(String name, BufferedImage image){
        float[] matrix = {
                1/16f, 1/8f, 1/16f,
                1/8f, 1/4f, 1/8f,
                1/16f, 1/8f, 1/16f,
        };

        float[] matrix2 = {
                1/273f, 4/273f, 7/273f, 4/273f, 1/273f,
                4/273f, 16/273f, 26/273f, 16/273f, 4/273f,
                7/273f, 26/273f, 41/273f, 26/273f, 7/273f,
                4/273f, 16/273f, 26/273f, 16/273f, 4/273f,
                1/273f, 4/273f, 7/273f, 4/273f, 1/273f,
        };

        BufferedImage original = image;
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix), ConvolveOp.EDGE_NO_OP, null);
        BufferedImage blurredImage = op.filter(original, null);

        //showBufferedImage(name, blurredImage);
        return blurredImage;
    }
}
