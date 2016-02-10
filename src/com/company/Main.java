package com.company;

import java.awt.*;
import java.util.ArrayList;

import com.company.database.DatabaseItem;
import com.company.database.DatabaseManager;
import com.company.model.*;
import com.company.model.Image;
import com.company.model.Rectangle;
import com.company.model.SDDImage;
import com.company.utils.ImageUtils;

public class Main {
    public static final int scaleWidth = 128;
    public static final int scaleHeight = 128;
    public static final int displayWindowWidth = LocalConfiguration.DISPLAY_WINDOW_WIDTH;
    public static final int displayWindowHeight = LocalConfiguration.DISPLAY_WINDOW_HEIGHT;
    public static final Color characterColor = Color.WHITE;
    public static final Color backgroundColor = Color.BLACK;

    public static ArrayList<SDDImage>  imagesList = new ArrayList<SDDImage>();
    public static DatabaseManager databaseManager = new DatabaseManager();

    public static void main(String[] args) {
        System.out.println("Création de la base de données d'images...");
        Main.databaseManager.selectAll(0);
        Main.loadImagesList();
        Main.applyGaussianBlur();
        Main.applyGrayScale();
        Main.applyScale(scaleWidth, scaleHeight);
        Main.applyBinarization(characterColor, backgroundColor);
        Main.applyImageColor();
        Main.applyMask(true);
        Main.applyCrop();
        System.out.println("Génération des attributs...");
        Main.generateAttributeSize();
        Main.generateAttributeFormat(0.90);
        Main.generateAttributeRelevantSurface();
        Main.generateAttributePixelRepartitions(9);
        Main.generateAttributeMiddleHorizontalCharacterLines();
        Main.generateAttributeMiddleVerticalCharacterLines();
        Main.generateAttributeOneThirdHorizontalCharacterLines();
        Main.generateAttributeOneThirdVerticalCharacterLines();
        Main.generateAttributeTwoThirdHorizontalCharacterLines();
        Main.generateAttributeTwoThirdVerticalCharacterLines();
        Main.generateAttributeVerticalCenterSymetry(0, 90);
        Main.generateAttributeHorizontalCenterSymetry(0, 90);
        Main.generateARFF();
        Main.showImagesList();
        System.out.println("Fin");
    }

    public static void loadImagesList(){
        System.out.println("Chargement des images...");
        for(DatabaseItem item :databaseManager.databaseItems) {
            imagesList.add(new SDDImage(item.name, new Image(ImageUtils.loadImage(item.path)), item.imageClass));
        }
    }

    public static ArrayList<Image> loadMasksList(){
        System.out.println("Chargement des marques...");
        ArrayList<Image> masksList = new ArrayList<>();
        for(DatabaseItem item :databaseManager.databaseItems) {
            masksList.add(new Image(ImageUtils.loadImage(item.maskPath)));
        }
        return masksList;
    }

    //== Pré-traitements sur les images

    public static void applyGaussianBlur() {
        System.out.println("application du floue gaussien...");
        for(SDDImage sddImage : imagesList) {
            sddImage.image.gaussianBlur();
        }
    }

    public static void applyGrayScale() {
        System.out.println("Transformation des images en niveaux de gris...");
        for(SDDImage sddImage : imagesList) {
            sddImage.image.grayScale();
        }
    }

    public static void applyBinarization(Color characterColor, Color backgroundColor) {
        System.out.println("Transformation des images en noir et blanc...");
        for(SDDImage sddImage : imagesList) {
            sddImage.image = sddImage.image.getBynaryImage(characterColor, backgroundColor);
        }
    }

    public static void applyScale(int width, int height) {
        System.out.println("Redimentionnement des des images en ["+width+" par "+height+"]...");
        for(SDDImage sddImage : imagesList) {
            sddImage.image.scale(width, height);
        }
    }

    public static void applyImageColor() {
        System.out.println("Inversion des couleurs si necessaire...");
        for(SDDImage sddImage : imagesList) {
            if(sddImage.image.computeImageBackgroundColor() != backgroundColor.getRGB()) {
                ((BinaryImage) sddImage.image).invertImageColors();
            }
        }
    }

    public static void applyMask(boolean scaleMask) {
        System.out.println("Application des masques sur les images...");
        ArrayList<Image> masksList = loadMasksList();
//        ImageUtils.showImageList(masksList, displayWindowWidth, displayWindowHeight);
        Image mask = null;
        for(int i=0; i < imagesList.size(); i++) {
            mask = masksList.get(i);
            if(scaleMask) {
                mask.scale(scaleWidth, scaleHeight);
            }
            ((BinaryImage)imagesList.get(i).image).applyMask(mask.bufferedImage);
        }
    }

    public static void applyCrop() {
        System.out.println("Crop des images...");
        for(SDDImage sddImage : imagesList) {
            Rectangle rectangle = sddImage.image.getBoundaries(characterColor.getRGB());
            sddImage.image.crop(rectangle);
        }
    }

    //== Utilitaires

    public static void showImagesList() {
        ArrayList<Image> images = new ArrayList<>();
        for(SDDImage sddImage : imagesList) {
            try {
                images.add((Image) sddImage.image.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        ImageUtils.showImageList(images, displayWindowWidth, displayWindowHeight);
    }

    public static void clear() {
        imagesList.clear();
    }

    //-- Génération d'attributs

    public static void generateARFF() {
        System.out.println("Génération du fichier ARFF...");
        ArrayList<ImageRelation> imageRelations = new ArrayList<>();
        for(SDDImage sddImage : imagesList) {
            sddImage.builder.setClasse(sddImage.imageClass);
            ImageRelation imageRelation = sddImage.builder.build();
            imageRelations.add(imageRelation);
        }
        ImageRelation.generateARFF(imageRelations);
    }

    /**
     * Get the format of the SDDImage
     * @return
     */
    public static void generateAttributeFormat(double thresholdPourcentage){
        for(SDDImage sddImage : imagesList) {
            FormatAttribute format = FormatAttribute.SQUARE;
            if(sddImage.image.getHeight() > sddImage.image.getWidth() + sddImage.image.getWidth() * thresholdPourcentage){
                format = FormatAttribute.VERTICAL_RECTANGLE;
            }else if (sddImage.image.getWidth() > sddImage.image.getHeight() + sddImage.image.getHeight() * thresholdPourcentage){
                format = FormatAttribute.HORIZONTAL_RECTANGLE;
            }
            sddImage.builder.setFormat(format);
        }
    }

    /**
     * Get the format of the SDDImage
     * @return
     */
    public static void generateAttributeRelevantSurface(){
        for(SDDImage sddImage : imagesList) {
            int[] colorsCount = ((BinaryImage)sddImage.image).countPixelsByColors();
            double surface = (float)colorsCount[0] / ((float)colorsCount[0] + (float)colorsCount[1]);
            sddImage.builder.setRelevantSurface(surface);
        }
    }

    /**
     * Get the format of the SDDImage
     * @return
     */
    public static void generateAttributeSize(){
        for(SDDImage sddImage : imagesList) {
            sddImage.builder.setSize(sddImage.image.getSize());
        }
    }

    public static void generateAttributeMiddleHorizontalCharacterLines() {
        for (SDDImage sddImage : imagesList) {
            int count = sddImage.image.getCharacterColorAreasAlongLine(
                    sddImage.image.getLeftMiddlePixel(),
                    sddImage.image.getRightMiddlePixel()
            );
            sddImage.builder.setMiddleHorizontalCharacterLines(count);
        }
    }

    public static void generateAttributeMiddleVerticalCharacterLines() {
        for (SDDImage sddImage : imagesList) {
            int count = sddImage.image.getCharacterColorAreasAlongLine(
                    sddImage.image.getTopMiddlePixel(),
                    sddImage.image.getBottomMiddlePixel()
            );
            sddImage.builder.setMiddleVerticalCharacterLines(count);
        }
    }

    public static void generateAttributeOneThirdHorizontalCharacterLines() {
        for (SDDImage sddImage : imagesList) {
            int count = sddImage.image.getCharacterColorAreasAlongLine(
                    sddImage.image.getLeftOneThirdPixel(),
                    sddImage.image.getRightOneThirdPixel()
            );
            sddImage.builder.setOneThirdHorizontalCharacterLines(count);
        }
    }

    public static void generateAttributeOneThirdVerticalCharacterLines() {
        for (SDDImage sddImage : imagesList) {
            int count = sddImage.image.getCharacterColorAreasAlongLine(
                    sddImage.image.getTopMiddlePixel(),
                    sddImage.image.getBottomMiddlePixel()
            );
            sddImage.builder.setOneThirdVerticalCharacterLines(count);
        }
    }

    public static void generateAttributeTwoThirdHorizontalCharacterLines() {
        for (SDDImage sddImage : imagesList) {
            int count = sddImage.image.getCharacterColorAreasAlongLine(
                    sddImage.image.getLeftOneThirdPixel(),
                    sddImage.image.getRightOneThirdPixel()
            );
            sddImage.builder.setTwoThirdHorizontalCharacterLines(count);
        }
    }

    public static void generateAttributeTwoThirdVerticalCharacterLines() {
        for (SDDImage sddImage : imagesList) {
            int count = sddImage.image.getCharacterColorAreasAlongLine(
                    sddImage.image.getTopMiddlePixel(),
                    sddImage.image.getBottomMiddlePixel()
            );
            sddImage.builder.setTwoThirdVerticalCharacterLines(count);
        }
    }

    /**
     * TODO : découper l'image en fonction du paramètre "split"
     * @param split
     */
    public static void generateAttributePixelRepartitions(int split) {
        for (SDDImage sddImage : imagesList) {
            Float[] pixelCount = ((BinaryImage)sddImage.image).getRepartition(characterColor.getRGB(), split);
            sddImage.builder.setCharacterPixelsRepartitionRatio(pixelCount);
        }
    }

    public static void generateAttributeVerticalCenterSymetry(int mode, int limit) {
        for (SDDImage sddImage : imagesList) {
            Float score = sddImage.image.getSymetryScore(sddImage.image.getTopMiddlePixel(), sddImage.image.getBottomMiddlePixel());
            if(mode == 0) { // pourcentage de symétrie
                sddImage.builder.setVerticalCenterSymetry(score);
            } else { // binaire :symétrique(1) ou non (0) en fonction de la "limite" donnée
                if(score * 100 >= limit) {
                    sddImage.builder.setVerticalCenterSymetry(1.0f);
                } else {
                    sddImage.builder.setVerticalCenterSymetry(0f);
                }
            }
        }
    }

    public static void generateAttributeHorizontalCenterSymetry(int mode, int limit) {
        for (SDDImage sddImage : imagesList) {
            Float score = sddImage.image.getSymetryScore(sddImage.image.getLeftMiddlePixel(), sddImage.image.getRightMiddlePixel());
            if(mode == 0) { // pourcentage de symétrie
                sddImage.builder.setHorizontalCenterSymetry(score);
            } else { // binaire :symétrique(1) ou non (0) en fonction de la "limite" donnée
                if(score * 100 >= limit) {
                    sddImage.builder.setHorizontalCenterSymetry(1.0f);
                } else {
                    sddImage.builder.setHorizontalCenterSymetry(0f);
                }
            }
        }
    }
}
