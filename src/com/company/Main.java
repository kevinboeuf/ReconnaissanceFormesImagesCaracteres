package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class Main {

    public static final String ATTRIBUTE_PACKAGE = "com.company";

    public static void main(String[] args) {
        ImageAttributes imageAttributes1 = new ImageAttributes(new Attribute<Integer>("size", AttributeType.INTEGER, 100), new Attribute<Integer>("relevantSurface", AttributeType.INTEGER, 25), new Attribute<ImageFormat>("format", AttributeType.INTEGER, ImageFormat.HORIZONTAL_RECTANGLE));
        ImageAttributes imageAttributes2 = new ImageAttributes(new Attribute<Integer>("size", AttributeType.INTEGER, 110), new Attribute<Integer>("relevantSurface", AttributeType.INTEGER, 26), new Attribute<ImageFormat>("format", AttributeType.INTEGER, ImageFormat.VERTICAL_RECTANGLE));
        List<ImageAttributes> imageAttributesList = new ArrayList<ImageAttributes>();
        imageAttributesList.add(imageAttributes1);
        imageAttributesList.add(imageAttributes2);
        printFile(imageAttributesList);
    }

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
