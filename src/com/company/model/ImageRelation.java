package com.company.model;

import com.company.utils.StringUtils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class ImageRelation {

    static final String RELATION_NAME = "ImageRelation";

    //Size of the image (width * height)
    @RelationAnnotation(type = AnnotationType.SINGLE_ENUM, singleType = ARFFType.NUMERIC)
    Integer size = -1;

    // Surface concerned by the character
    @RelationAnnotation(type = AnnotationType.SINGLE_ENUM, singleType = ARFFType.NUMERIC)
    Double relevantSurface = -1.0;

    // Horizontal / Vertical rectangle or square
    @RelationAnnotation(type = AnnotationType.MULTIPLE_ENUM, multipleType = FormatAttribute.class)
    FormatAttribute format = FormatAttribute.HORIZONTAL_RECTANGLE;

    @RelationAnnotation(isDynamic = true, type = AnnotationType.SINGLE_ENUM, singleType = ARFFType.NUMERIC)
    List<Float> characterPixelsRepartitionRatio = new ArrayList<>();

    @RelationAnnotation(type = AnnotationType.SINGLE_ENUM, singleType = ARFFType.NUMERIC)
    int horizontalCharacterLines = -1;

    @RelationAnnotation(type = AnnotationType.SINGLE_ENUM, singleType = ARFFType.NUMERIC)
    int verticalCharacterLines = -1;

    @RelationAnnotation(type = AnnotationType.SINGLE_ENUM, singleType = ARFFType.NUMERIC)
    float horizontalCenterSymetry = -1;

    @RelationAnnotation(type = AnnotationType.SINGLE_ENUM, singleType = ARFFType.NUMERIC)
    float verticalCenterSymetry = -1;

    @RelationAnnotation(isClass = true, type = AnnotationType.MULTIPLE_ENUM, multipleType = ImageClass.class)
    ImageClass classe = ImageClass.ZERO;

    public ImageRelation(Integer size, Double relevantSurface, FormatAttribute format, List<Float> characterPixelsRepartitionRatio, int horizontalCharacterLines, int verticalCharacterLines, float horizontalCenterSymetry, float verticalCenterSymetry, ImageClass classe) {
        this.size = size;
        this.relevantSurface = relevantSurface;
        this.format = format;
        this.characterPixelsRepartitionRatio = characterPixelsRepartitionRatio;
        this.horizontalCharacterLines = horizontalCharacterLines;
        this.verticalCharacterLines = verticalCharacterLines;
        this.horizontalCenterSymetry = horizontalCenterSymetry;
        this.verticalCenterSymetry = verticalCenterSymetry;
        this.classe = classe;
    }

    public static List<String> attributesEnabled = new ArrayList<>();

    /**
     * Types simples (natifs) d'attributs utilisés par le format arff
     */
    public enum ARFFType {
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

                if(attributesEnabled.contains(field.getName())) {
                    Annotation annotation = field.getAnnotation(RelationAnnotation.class);
                    RelationAnnotation relationAnnotation = (RelationAnnotation) annotation;

                    String typeValues = "";
                    String attributeName = "";

                    if (relationAnnotation.type().equals(AnnotationType.SINGLE_ENUM)) {
                        typeValues = relationAnnotation.singleType().toString();
                    } else if (relationAnnotation.type().equals(AnnotationType.SINGLE_STRING)) {
                        typeValues = relationAnnotation.singleStringType();
                    } else if (relationAnnotation.type().equals(AnnotationType.MULTIPLE_ENUM)) {
                        Enum[] enumTypes = relationAnnotation.multipleType().getEnumConstants();
                        typeValues = "{" + StringUtils.joinEnum(enumTypes, SEPARATOR) + "}";
                    } else if (relationAnnotation.type().equals(AnnotationType.MULTIPLE_ARRAY)) {
                        String[] stringTypes = relationAnnotation.multipleArrayType();
                        List<String> stringList = Arrays.asList(stringTypes);
                        typeValues = "{" + StringUtils.joinStringArray(stringList, SEPARATOR) + "}";
                    }

                    if (relationAnnotation.isDynamic()) {
                        if (imageRelationList.size() > 0) {
                            try {
                                int size = ((Collection) field.get(imageRelationList.get(0))).size();
                                for (int i = 0; i < size; i++) {
                                    writer.println("@ATTRIBUTE " + field.getName() + i + " " + typeValues);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (relationAnnotation.isClass()) {
                            attributeName = "class";
                        } else {
                            attributeName = field.getName();
                        }
                        writer.println("@ATTRIBUTE " + attributeName + " " + typeValues);
                    }
                }
            }
        }


        // print attributes data
        writer.println("\n@DATA");
        for (ImageRelation imageRelation : imageRelationList){
            List<String> attributes = new ArrayList<>();
            for (Field field : obj.getDeclaredFields()) {
                if (field.isAnnotationPresent(RelationAnnotation.class)) {
                    if(attributesEnabled.contains(field.getName())) {
                        try {
                            if(field.get(imageRelation) instanceof Collection) {
                                Iterator iterator = ((Collection)field.get(imageRelation)).iterator();
                                while (iterator.hasNext()) {
                                    attributes.add(iterator.next().toString());
                                }
                            } else {
                                attributes.add(field.get(imageRelation).toString());
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
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
        int horizontalCharacterLines = 0;
        int verticalCharacterLines = 0;
        List<Float> characterPixelsRepartitionRatio = new ArrayList<>();
        float verticalCenterSymetry = -1f;
        float horizontalCenterSymetry = -1f;

        ImageClass classe = ImageClass.A;

        Class<ImageRelation> obj = ImageRelation.class;

        public Builder() {

        }

        public ImageRelation.Builder setSize(Integer size) {
            this.size = size;
            enable("size");
            return this;
        }

        public ImageRelation.Builder setRelevantSurface(Double relevantSurface) {
            this.relevantSurface = relevantSurface;
            enable("relevantSurface");
            return this;
        }

        public ImageRelation.Builder setFormat(FormatAttribute format) {
            this.format = format;
            enable("format");
            return this;
        }

        public ImageRelation.Builder setClasse(ImageClass classe) {
            this.classe = classe;
            enable("classe");
            return this;
        }

        public ImageRelation.Builder setCharacterPixelsRepartitionRatio(Float[] characterPixelsRepartitionRatio) {
            this.characterPixelsRepartitionRatio = Arrays.asList(characterPixelsRepartitionRatio);
            enable("characterPixelsRepartitionRatio");
            return this;
        }

        public ImageRelation.Builder setHorizontalCharacterLines(int horizontalCharacterLines) {
            this.horizontalCharacterLines = horizontalCharacterLines;
            enable("horizontalCharacterLines");
            return this;
        }

        public ImageRelation.Builder setVerticalCharacterLines(int verticalCharacterLines) {
            this.verticalCharacterLines = verticalCharacterLines;
            enable("verticalCharacterLines");
            return this;
        }

        public ImageRelation.Builder setHorizontalCenterSymetry(Float horizontalCenterSymetry) {
            this.horizontalCenterSymetry = horizontalCenterSymetry;
            enable("horizontalCenterSymetry");
            return this;
        }

        public ImageRelation.Builder setVerticalCenterSymetry(Float verticalCenterSymetry) {
            this.verticalCenterSymetry = verticalCenterSymetry;
            enable("verticalCenterSymetry");
            return this;
        }

        public void enable(String fieldToEnable) {
            obj.getDeclaredFields();
            for (Field field : obj.getDeclaredFields()) {
                if (field.isAnnotationPresent(RelationAnnotation.class)) {
                    if(fieldToEnable.equals(field.getName())) {
                        if(!attributesEnabled.contains(field.getName())) {
                            attributesEnabled.add(field.getName());
                        }
                    }
                }
            }
        }

        public ImageRelation build(){
            return new ImageRelation(size, relevantSurface, format, characterPixelsRepartitionRatio, horizontalCharacterLines, verticalCharacterLines, horizontalCenterSymetry, verticalCenterSymetry, classe);
        }

    }
}