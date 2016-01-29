package com.company;

import com.company.model.ImageClass;

import java.util.ArrayList;

public class DatabaseManager {

    public static final String IMAGE_PATH = LocalConfiguration.FOLDER;
    public static final String MASK_PATH = LocalConfiguration.MASKFOLDER;
    ArrayList<DatabaseItem> databaseItems = new ArrayList<>();

    public DatabaseManager() {

    }

    public DatabaseManager selectAll() {
        for(ImageClass imageClass : ImageClass.values()) {
            DatabaseFolder folder = new DatabaseFolder(IMAGE_PATH + imageClass.path + "/", imageClass, MASK_PATH + imageClass.path + "/");
            addFolder(folder);
        }
        return this;
    }

    public DatabaseManager selectRange(ImageClass imageClass1, ImageClass imageClass2) {
        int count = Math.abs(imageClass1.number - imageClass2.number) + 1;
        ImageClass startClass = imageClass1;
        if(imageClass2.number <= imageClass1.number) {
            startClass = imageClass2;
        }

        for(int i = startClass.number; i <= count; i++) {
            ImageClass imageClass = ImageClass.getImageClassFromNumber(i);
            addFolder(new DatabaseFolder(IMAGE_PATH + imageClass.path + "/", imageClass, MASK_PATH + imageClass.path + "/"));
        }
        return this;
    }

    public DatabaseManager select(ImageClass imageClass) {
        addFolder(new DatabaseFolder(IMAGE_PATH + imageClass.path + "/", imageClass, MASK_PATH + imageClass.path + "/"));
        return this;
    }

    public DatabaseManager selectCustom(String path, String name, ImageClass imageClass, String maskPath) {
        DatabaseItem item = new DatabaseItem(path, name, imageClass, maskPath);
        addUnique(item);
        return this;
    }

    public boolean contains(DatabaseItem databaseItem) {
        boolean result = false;
        for(DatabaseItem item : databaseItems) {
            if(item.toString().equals(databaseItem.toString())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void addUnique(DatabaseItem item) {
        if(!contains(item)) {
            databaseItems.add(item);
        }
    }

    private void addFolder(DatabaseFolder databaseFolder) {
        for(DatabaseItem item : databaseFolder.databaseItems) {
            addUnique(item);
        }
    }
}
