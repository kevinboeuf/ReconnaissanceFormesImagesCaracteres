package com.company.tests;

import com.company.LocalConfiguration;
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
            Main.databaseManager.selectRange(ImageClass.ZERO, ImageClass.z, 0);
            Main.loadImagesList();
            Main.applyGaussianBlur();
            Main.applyGrayScale();
            Main.applyScale(scaleWidth, scaleHeight);
            Main.applyBinarization(characterColor, backgroundColor);
            Main.applyImageColor();
            Main.applyMask(true);
            Main.applyCrop();
            Main.showImagesList();
//            Main.generateAttributeFormat(0.30);
//            Main.generateAttributeRelevantSurface();
            Main.generateAttributePixelRepartitions(9);
            Main.generateAttributeHorizontalCharacterLines();
            Main.generateAttributeVerticalCharacterLines();
            Main.generateAttributeVerticalCenterSymetry(0, 90);
            Main.generateAttributeHorizontalCenterSymetry(0, 90);
            Main.generateARFF();
        }
    }
}
