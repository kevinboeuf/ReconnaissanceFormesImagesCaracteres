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
            System.out.println("Création de la base de données d'images...");
            Main.databaseManager.selectRange(ImageClass.ZERO, ImageClass.NEUF, 0);
            Main.loadImagesList();
            Main.applyGaussianBlur();
            Main.applyGrayScale();
            Main.applyScale(scaleWidth, scaleHeight);
            Main.applyBinarization(characterColor, backgroundColor);
            Main.applyImageColor();
            Main.applyMask(true);
            Main.applyCrop();
            System.out.println("Génération des attributs...");
//            Main.generateAttributeSize();
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
    }
}
