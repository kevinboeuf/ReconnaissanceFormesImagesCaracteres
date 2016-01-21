package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

        //Get the list of images
        List<ImageRelation> imageRelationAttributesList = new ArrayList<ImageRelation>();
        HashMap <String, BufferedImage> imagesList = getImagesList(1, 62);

        //Show the images
        initDrawingFrame();
        for (Map.Entry<String, BufferedImage> entry : imagesList.entrySet()){
            showBufferedImage(entry.getKey(), entry.getValue());
        }

        BufferedImage image;

        //Extract the data
        for (Map.Entry<String, BufferedImage> entry : imagesList.entrySet()){
            image = entry.getValue();
            imageRelationAttributesList.add(getImageRelation(image));
        }

        //Generate the ARFF
        ImageRelation.generateARFF(imageRelationAttributesList);
    }

    public static ImageRelation getImageRelation(BufferedImage image){
        int size = getSize(image);
        FormatAttribute format = getFormat(image);
        ImageRelation.Builder builder = new ImageRelation.Builder();
        return builder.setSize(size).setFormat(format).build();
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
    public static HashMap<String, BufferedImage> getImagesList(int beginningIndex, int endingIndex){
        HashMap <String, BufferedImage> imagesList = new HashMap <String, BufferedImage>();
        BufferedImage img = null;
        List<String> results = null;

        for (int i = beginningIndex; i <= endingIndex; i++) {
            File[] files = new File(LocalConfiguration.Folder + "Sample" + String.format("%03d", i)).listFiles();
            System.out.println("Fichiers contenus dans le dossier : " + LocalConfiguration.Folder + "Sample" + String.format("%03d", i));
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