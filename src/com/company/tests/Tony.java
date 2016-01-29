package com.company.tests;

import com.company.Main;
import com.company.model.ImageClass;

import java.awt.*;

public class Tony {

    public static final Color characterColor = Color.WHITE;
    public static final Color backgroundColor = Color.BLACK;

    public static final int scaleWidth = 128;
    public static final int scaleHeight = 128;

    public static void main(String[] args) {
        runTest(0);
    }

    public static void runTest(int testNumber) {
        if(testNumber == 0) {
            Main.databaseManager.select(ImageClass.ZERO);
            Main.loadImagesList();
            Main.applyGaussianBlur();
            Main.applyGrayScale();
            Main.applyScale(scaleWidth, scaleHeight);
            Main.applyBinarization(characterColor, backgroundColor);
            Main.applyImageColor();
            Main.applyMask(true);
            Main.applyCrop();
            Main.showImagesList();
            Main.computeImagesListRelations();
        } else if(testNumber == 1) {
            Main.databaseManager.selectRange(ImageClass.ZERO, ImageClass.Z);
            Main.loadImagesList();
            Main.applyGaussianBlur();
            Main.applyGrayScale();
            Main.applyScale(scaleWidth, scaleHeight);
            Main.applyBinarization(characterColor, backgroundColor);
            Main.applyImageColor();
            Main.applyMask(true);
            Main.applyCrop();
            Main.showImagesList();
            Main.computeImagesListRelations();
        }
    }
}
