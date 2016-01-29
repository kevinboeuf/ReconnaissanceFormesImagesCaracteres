package com.company.tests;

import com.company.Execute;
import com.company.model.ImageClass;

import java.awt.*;

public class Kevin {

    public static final Color characterColor = Color.WHITE;
    public static final Color backgroundColor = Color.BLACK;

    public static final int scaleWidth = 128;
    public static final int scaleHeight = 128;

    public static void main(String[] args) {
        runTest(0);
    }

    public static void runTest(int testNumber) {
        if(testNumber == 0) {
            Execute.databaseManager.select(ImageClass.ZERO);
            Execute.loadImagesList();
            Execute.applyGaussianBlur();
            Execute.applyGrayScale();
            Execute.applyScale(scaleWidth, scaleHeight);
            Execute.applyBinarization(characterColor, backgroundColor);
            Execute.applyImageColor();
            Execute.applyMask(true);
            Execute.applyCrop();
            Execute.showImagesList();
            Execute.computeImagesListRelations();
        }
    }
}
