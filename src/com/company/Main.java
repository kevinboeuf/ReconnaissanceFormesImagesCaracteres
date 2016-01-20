package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final String ATTRIBUTE_PACKAGE = "com.company";
    public static final String FOLDER = "C://Users/Kevin/Desktop/IMR3/Reco_formes/English/Img/GoodImg/Bmp/";
    public static JFrame jframe = new JFrame();
    public static Container pane = new Container();
    public static ImagePanel imagePanel = new ImagePanel();

    public static void main(String[] args) {
        /*
        ImageAttributes imageAttributes1 = new ImageAttributes(new Attribute<Integer>("size", AttributeType.INTEGER, 100), new Attribute<Integer>("relevantSurface", AttributeType.INTEGER, 25), new Attribute<ImageFormat>("format", AttributeType.INTEGER, ImageFormat.HORIZONTAL_RECTANGLE));
        ImageAttributes imageAttributes2 = new ImageAttributes(new Attribute<Integer>("size", AttributeType.INTEGER, 110), new Attribute<Integer>("relevantSurface", AttributeType.INTEGER, 26), new Attribute<ImageFormat>("format", AttributeType.INTEGER, ImageFormat.VERTICAL_RECTANGLE));
        List<ImageAttributes> imageAttributesList = new ArrayList<ImageAttributes>();
        imageAttributesList.add(imageAttributes1);
        imageAttributesList.add(imageAttributes2);
        */
        HashMap <String, BufferedImage> imagesList = getImagesList(1, 20);
        initDrawingFrame();
        for (Map.Entry<String, BufferedImage> entry : imagesList.entrySet()){
            showBufferedImage(entry.getKey(), entry.getValue());
        }
        //printFile(imageAttributesList);
    }

    /**
     * Reads all the images from the constant folder and returns it as a list of BufferedImages
     * @param beginningIndex
     * @param endingIndex
     * @return
     */
    public static HashMap<String, BufferedImage> getImagesList(int beginningIndex, int endingIndex){
        HashMap <String, BufferedImage> imagesList = new HashMap <String, BufferedImage>();
        BufferedImage img = null;
        List<String> results = null;

        for (int i = beginningIndex; i <= endingIndex; i++) {
            File[] files = new File(FOLDER + "Sample" + String.format("%03d", i)).listFiles();
            System.out.println("Fichiers contenus dans le dossier : " + FOLDER + "Sample" + String.format("%03d", i));
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                    try {
                        img = ImageIO.read(file);
                        imagesList.put(file.getName(), img);
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

    public static void initDrawingFrame(){
        jframe.setSize(250, 250);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        imagePanel = new ImagePanel();
        pane = jframe.getContentPane();
        pane.add(imagePanel);
        jframe.setVisible(true);
    }

    /**
     * Generate the ARFF file
     * @param list
     */
    public static void printFile(List<ImageAttributes> list) {
        final String SEPARATOR = ",";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("imagesData.arff", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println("% 1. Title: Images attributes Database\n%\n" +
                "% 2. Sources:\n" +
                "%      (a) Creators: Boeuf Kevin & Hennequin Tony\n" +
                "%      (b) Date: January, 18, 2016\n" +
                "% " +
                "@RELATION imagesData\n");

        for (Field i : ImageAttributes.class.getDeclaredFields()){
            String type = "";
            if(i.getGenericType().toString().equals(ATTRIBUTE_PACKAGE + ".Attribute<java.lang.Integer>")){
                type = AttributeType.INTEGER.type;
            }else if (i.getGenericType().toString().equals(ATTRIBUTE_PACKAGE + ".Attribute<java.lang.String>")){
                type = AttributeType.STRING.type;
            }else if (i.getGenericType().toString().equals(ATTRIBUTE_PACKAGE + ".Attribute<" + ATTRIBUTE_PACKAGE + ".ImageFormat>")){
                ImageFormat[] types = ImageFormat.values();
                type = "{";
                int j = 0;
                for (ImageFormat imageFormat : types) {
                    j++;
                    type += imageFormat.name();
                    if(j != types.length)
                        type += ",";
                }
                type+="}";
            }else{
                type = AttributeType.STRING.type;
                ImageFormat.values();
            }
            writer.println("@ATTRIBUTE " + i.getName() + " " + type);
        }

        writer.println("\n@DATA");
        for (ImageAttributes i : list){
            writer.println(i.size.value + SEPARATOR + i.relevantSurface.value+ SEPARATOR + i.format.value);
        }
        writer.close();
    }
}
