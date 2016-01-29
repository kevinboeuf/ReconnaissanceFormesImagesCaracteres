package com.company;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import com.company.model.*;
import com.company.model.Image;
import com.company.model.Rectangle;
import com.company.model.SDDImage;
import com.company.utils.ImageUtils;

public class Execute {

    public static final int beginningIndex = 1;
    public static final int endingIndex = 1;
    public static final int scaleWidth = 128;
    public static final int scaleHeight = 128;
    public static final int displayWindowWidth = LocalConfiguration.DISPLAY_WINDOW_WIDTH;
    public static final int displayWindowHeight = LocalConfiguration.DISPLAY_WINDOW_HEIGHT;
    public static final Color characterColor = Color.WHITE;
    public static final Color backgroundColor = Color.BLACK;

    public static ArrayList<SDDImage>  imagesList = new ArrayList<SDDImage>();
    public static ArrayList<ImageRelation>  imagesListRelations = new ArrayList<ImageRelation>();
    private static DatabaseManager databaseManager = new DatabaseManager();

    public static void main(String[] args) {
        runTest(1);
    }

    public static void runTest(int testNumber) {
         if(testNumber == 0) {
             databaseManager.selectAll();
             loadImagesList();
             applyGaussianBlur();
             applyGrayScale();
             applyBinarization(characterColor, backgroundColor);
             applyImageColor();
             applyMask(false);
             applyScale(scaleWidth, scaleHeight);
             applyCrop();
             showImagesList();
             computeImagesListRelations();
         } else if(testNumber == 1) {
             databaseManager.select(ImageClass.TROIS).select(ImageClass.HUIT);
             loadImagesList();
             applyGaussianBlur();
             applyGrayScale();
             applyBinarization(characterColor, backgroundColor);
             applyImageColor();
             applyMask(false);
             applyScale(scaleWidth, scaleHeight);
             applyCrop();
             showImagesList();
             computeImagesListRelations();
        } else if(testNumber == 2) {
             databaseManager.selectRange(ImageClass.ZERO, ImageClass.CINQ).select(ImageClass.A);
             loadImagesList();
             applyGaussianBlur();
             applyGrayScale();
             applyBinarization(characterColor, backgroundColor);
             applyImageColor();
             applyMask(false);
             applyScale(scaleWidth, scaleHeight);
             applyCrop();
             showImagesList();
             computeImagesListRelations();
        }
    }

    public static void loadImagesList(){
        for(DatabaseItem item :databaseManager.databaseItems) {
            imagesList.add(new SDDImage(item.name, new Image(ImageUtils.loadImage(item.path)), item.imageClass));
        }
    }

    public static ArrayList<Image> loadMasksList(){
        ArrayList<Image> masksList = new ArrayList<>();
        for(DatabaseItem item :databaseManager.databaseItems) {
            masksList.add(new Image(ImageUtils.loadImage(item.maskPath)));
        }
        return masksList;
    }

    public static void applyGaussianBlur() {
        for(SDDImage sddImage : imagesList) {
            sddImage.image.gaussianBlur();
        }
    }

    public static void applyGrayScale() {
        for(SDDImage sddImage : imagesList) {
            sddImage.image.grayScale();
        }
    }

    public static void applyBinarization(Color characterColor, Color backgroundColor) {
        for(SDDImage sddImage : imagesList) {
            sddImage.image = sddImage.image.getBynaryImage(characterColor, backgroundColor);
        }
    }

    public static void applyScale(int width, int height) {
        for(SDDImage sddImage : imagesList) {
            sddImage.image.scale(width, height);
        }
    }

    public static void applyImageColor() {
        for(SDDImage sddImage : imagesList) {
            if(sddImage.image.computeImageBackgroundColor() != backgroundColor.getRGB()) {
                ((BinaryImage) sddImage.image).invertImageColors();
            }
        }
    }

    public static void applyMask(boolean scaleMask) {
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

    public static void applyCrop() {
        for(SDDImage sddImage : imagesList) {
            Rectangle rectangle = sddImage.image.getBoundaries(characterColor.getRGB());
            sddImage.image.crop(rectangle);
        }
    }

    //-- Images relations methods

    public static void computeImagesListRelations() {
        for(SDDImage SDDImage : imagesList) {
            imagesListRelations.add(getImageRelation(SDDImage));
        }
        ImageRelation.generateARFF(imagesListRelations);
    }

    public static ImageRelation getImageRelation(SDDImage SDDImage) {
        int size = SDDImage.image.getSize();
        FormatAttribute format = getFormat(SDDImage);
        int[] pixelCount = ((BinaryImage)SDDImage.image).getRepartition(characterColor.getRGB());
        int topLeftPixelCount = pixelCount[0];
        int topRightPixelCount = pixelCount[1];
        int bottomLeftPixelCount = pixelCount[2];
        int bottomRightPixelCount = pixelCount[3];

        ImageRelation.Builder builder = new ImageRelation.Builder();
        return builder.setSize(size)
                .setFormat(format)
                .setTopLeftPixelCount(topLeftPixelCount)
                .setTopRightPixelCount(topRightPixelCount)
                .setBottomLeftPixelCount(bottomLeftPixelCount)
                .setBottomRightPixelCount(bottomRightPixelCount)
                .setClasse(SDDImage.imageClass)
                .build();
    }

    /**
     * Get the format of the SDDImage
     * @param SDDImage
     * @return
     */
    public static FormatAttribute getFormat(SDDImage SDDImage){
        FormatAttribute format = FormatAttribute.SQUARE;
        if(SDDImage.image.getHeight() > SDDImage.image.getWidth()){
            format = FormatAttribute.VERTICAL_RECTANGLE;
        }else if (SDDImage.image.getWidth() > SDDImage.image.getHeight()){
            format = FormatAttribute.HORIZONTAL_RECTANGLE;
        }
        return format;
    }

}
