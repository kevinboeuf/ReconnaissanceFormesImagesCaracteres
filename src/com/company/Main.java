package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
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

        List<ImageRelation> imageRelationAttributesList = new ArrayList<ImageRelation>();
        imageRelationAttributesList.add(new ImageRelation(100, 25, FormatAttribute.HORIZONTAL_RECTANGLE));
        imageRelationAttributesList.add(new ImageRelation(110, 26, FormatAttribute.VERTICAL_RECTANGLE));
        ImageRelation.generateARFF(imageRelationAttributesList);

        /*HashMap <String, BufferedImage> imagesList = getImagesList(1, 20);
        initDrawingFrame();
        for (Map.Entry<String, BufferedImage> entry : imagesList.entrySet()){
            showBufferedImage(entry.getKey(), entry.getValue());
        }*/
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
}
