package com.company.model;

import com.company.utils.StringUtils;

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
    @RelationAnnotation(type = SimpleAttributeType.STRING)
    FormatAttribute format;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    Integer topLeftPixelCount;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    Integer topRightPixelCount;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    Integer bottomLeftPixelCount;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    Integer bottomRightPixelCount;

    @RelationAnnotation(classe = ImageClass.class)
    ImageClass classe = ImageClass.A;

    public ImageRelation(Integer size, Integer relevantSurface, FormatAttribute format, int topLeftPixelCount,  int topRightPixelCount, int bottomLeftPixelCount, int bottomRightPixelCount, ImageClass classe) {
        this.size = size;
        this.relevantSurface = relevantSurface;
        this.format = format;
        this.topLeftPixelCount = topLeftPixelCount;
        this.topRightPixelCount = topRightPixelCount;
        this.bottomLeftPixelCount = bottomLeftPixelCount;
        this.bottomRightPixelCount = bottomRightPixelCount;
        this.classe = classe;
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
     * Génère un fichier arff de cette path avec la liste de données fournie
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
                    writer.println("@ATTRIBUTE class {"+ StringUtils.joinEnum(enumTypes, SEPARATOR)+"}");
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

    public static class Builder{

        Integer size = -1;
        Integer relevantSurface = -1;
        FormatAttribute format = FormatAttribute.SQUARE;
        Integer topLeftPixelCount = -1;
        Integer topRightPixelCount = -1;
        Integer bottomLeftPixelCount = -1;
        Integer bottomRightPixelCount = -1;

        ImageClass classe = ImageClass.A;

        public Builder() {

        }

        public ImageRelation.Builder setSize(Integer size) {
            this.size = size;
            return this;
        }

        public ImageRelation.Builder setRelevantSurface(Integer relevantSurface) {
            this.relevantSurface = relevantSurface;
            return this;
        }

        public ImageRelation.Builder setFormat(FormatAttribute format) {
            this.format = format;
            return this;
        }

        public ImageRelation.Builder setClasse(ImageClass classe) {
            this.classe = classe;
            return this;
        }

        public ImageRelation.Builder setTopLeftPixelCount(Integer characterTopLeftPixelCount) {
            this.topLeftPixelCount = characterTopLeftPixelCount;
            return this;
        }

        public ImageRelation.Builder setTopRightPixelCount(Integer characterTopRightPixelCount) {
            this.topRightPixelCount = characterTopRightPixelCount;
            return this;
        }

        public ImageRelation.Builder setBottomLeftPixelCount(Integer characterBottomLeftPixelCount) {
            this.bottomLeftPixelCount = characterBottomLeftPixelCount;
            return this;
        }

        public ImageRelation.Builder setBottomRightPixelCount(Integer getCharacterBottomRightPixelCount) {
            this.bottomRightPixelCount = getCharacterBottomRightPixelCount;
            return this;
        }

        public ImageRelation build(){
            return new ImageRelation(size, relevantSurface, format, topLeftPixelCount, topRightPixelCount, bottomLeftPixelCount, bottomRightPixelCount, classe);
        }

    }
}