package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ImageRelation {

    static final String RELATION_NAME = "ImageRelation";

    //Size of the image (width * height)
    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    Integer size;

    // Surface concerned by the character
    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    Integer relevantSurface;

    // Horizontal / Vertical rectangle or square
    @RelationAnnotation(classe = FormatAttribute.class)
    FormatAttribute format;

    public ImageRelation(Integer size, Integer relevantSurface, FormatAttribute format) {
        this.size = size;
        this.relevantSurface = relevantSurface;
        this.format = format;
    }

    /**
     * Types simples (natifs) d'attributs utilisés par le format arff
     */
    public enum SimpleAttributeType {
        NONE,
        NUMERIC,
        STRING,
        DATE
    }

    /**
     * Génère un fichier arff de cette classe avec la liste de données fournie
     * @param imageRelationList
     */
    public static void generateARFF(List<ImageRelation> imageRelationList) {
        Class<ImageRelation> obj = ImageRelation.class;
        PrintWriter writer = null;
        final String SEPARATOR = ",";

        try {
            writer = new PrintWriter(ImageRelation.RELATION_NAME + ".arff", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // print header
        writer.println("% 1. Title: Images attributes Database\n%\n" +
                "% 2. Sources:\n" +
                "%      (a) Creators: Boeuf Kevin & Hennequin Tony\n" +
                "%      (b) Date: January, 18, 2016\n" +
                "%\n\n" +
                "@RELATION "+ ImageRelation.RELATION_NAME +"\n");

        // print attributes declarations
        for (Field field : obj.getDeclaredFields()) {
            if (field.isAnnotationPresent(RelationAnnotation.class)) {

                Annotation annotation = field.getAnnotation(RelationAnnotation.class);
                RelationAnnotation relationAnnotation = (RelationAnnotation) annotation;

                if(!relationAnnotation.type().equals(SimpleAttributeType.NONE)){
                    writer.println("@ATTRIBUTE " + field.getName() + " " + relationAnnotation.type());
                } else if(relationAnnotation.classe() != Enum.class) {
                    Enum[] enumTypes = relationAnnotation.classe().getEnumConstants();
                    writer.println("@ATTRIBUTE " + field.getName() + " {"+StringUtils.joinEnum(enumTypes, SEPARATOR)+"}");
                }
            }
        }

        // print attributes data
        writer.println("\n@DATA");
        for (ImageRelation imageRelation : imageRelationList){
            List<String> attributes = new ArrayList<>();
            for (Field field : obj.getDeclaredFields()) {
                if (field.isAnnotationPresent(RelationAnnotation.class)) {
                    try {
                        attributes.add(field.get(imageRelation).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            writer.println(StringUtils.joinStringArray(attributes, SEPARATOR));
        }
        writer.close();
    }
}