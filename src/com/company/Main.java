package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ImageAttributes imageAttributes1 = new ImageAttributes(100, 25, ImageFormat.HORIZONTAL_RECTANGLE);
        ImageAttributes imageAttributes2 = new ImageAttributes(110, 26, ImageFormat.VERTICAL_RECTANGLE);
        List<ImageAttributes> imageAttributesList = new ArrayList<ImageAttributes>();
        imageAttributesList.add(imageAttributes1);
        imageAttributesList.add(imageAttributes2);
        printFile(imageAttributesList);
    }

    public static void printFile(List<ImageAttributes> list) {
        String separator = ",";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("imagesData.arff", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (ImageAttributes i : list){
            writer.println(i.format + separator + i.relevantSurface + separator + i.size);
        }
        writer.close();
    }
}
