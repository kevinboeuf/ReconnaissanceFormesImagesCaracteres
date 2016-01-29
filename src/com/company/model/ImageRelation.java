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
    Double relevantSurface;

    // Horizontal / Vertical rectangle or square
    @RelationAnnotation(complexType = FormatAttribute.class)
    FormatAttribute format;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    float topLeftPixelRatio;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    float topRightPixelRatio;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    float bottomLeftPixelRatio;

    @RelationAnnotation(type = SimpleAttributeType.NUMERIC)
    float bottomRightPixelRatio;

    @RelationAnnotation(classe = ImageClass.class)
    ImageClass classe = ImageClass.A;

    public ImageRelation(Integer size, Double relevantSurface, FormatAttribute format, float topLeftPixelRatio,  float topRightPixelRatio, float bottomLeftPixelRatio, float bottomRightPixelRatio, ImageClass classe) {
        this.size = size;
        this.relevantSurface = relevantSurface;
        this.format = format;
        this.topLeftPixelRatio = topLeftPixelRatio;
        this.topRightPixelRatio = topRightPixelRatio;
        this.bottomLeftPixelRatio = bottomLeftPixelRatio;
        this.bottomRightPixelRatio = bottomRightPixelRatio;
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
                Enum[] enumTypes;

                if(!relationAnnotation.complexType().equals(Enum.class)) {
                    enumTypes = relationAnnotation.complexType().getEnumConstants();
                    writer.println("@ATTRIBUTE " + field.getName() + " {"+ StringUtils.joinEnum(enumTypes, SEPARATOR)+"}");
                } else if(!relationAnnotation.type().equals(SimpleAttributeType.NONE)){
                    writer.println("@ATTRIBUTE " + field.getName() + " " + relationAnnotation.type());
                } else if(relationAnnotation.classe() != Enum.class) {
                    enumTypes = relationAnnotation.classe().getEnumConstants();
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
        Double relevantSurface = 0.0;
        FormatAttribute format = FormatAttribute.SQUARE;
        float topLeftPixelRatio = -1;
        float topRightPixelRatio = -1;
        float bottomLeftPixelRatio = -1;
        float bottomRightPixelRatio = -1;

        ImageClass classe = ImageClass.A;

        public Builder() {

        }

        public ImageRelation.Builder setSize(Integer size) {
            this.size = size;
            return this;
        }

        public ImageRelation.Builder setRelevantSurface(Double relevantSurface) {
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

        public ImageRelation.Builder setTopLeftPixelCount(float characterTopLeftPixelRatio) {
            this.topLeftPixelRatio = characterTopLeftPixelRatio;
            return this;
        }

        public ImageRelation.Builder setTopRightPixelCount(float characterTopRightPixelRatio) {
            this.topRightPixelRatio = characterTopRightPixelRatio;
            return this;
        }

        public ImageRelation.Builder setBottomLeftPixelCount(float characterBottomLeftPixelRatio) {
            this.bottomLeftPixelRatio= characterBottomLeftPixelRatio;
            return this;
        }

        public ImageRelation.Builder setBottomRightPixelCount(float characterBottomRightPixelRatio) {
            this.bottomRightPixelRatio= characterBottomRightPixelRatio;
            return this;
        }

        public ImageRelation build(){
            return new ImageRelation(size, relevantSurface, format, topLeftPixelRatio, topRightPixelRatio, bottomLeftPixelRatio, bottomRightPixelRatio, classe);
        }

    }
}