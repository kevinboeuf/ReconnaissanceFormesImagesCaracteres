package com.company.database;

import com.company.model.ImageClass;

import java.io.File;
import java.util.ArrayList;

public class DatabaseFolder {
    public String maskPath = "";
    public String path = "";
    public ImageClass imageClass = ImageClass.ZERO;
    ArrayList<DatabaseItem> databaseItems = new ArrayList<>();

    public DatabaseFolder(String imagesPath, ImageClass imageClass, String maskPath, int limit) {
        this.path = imagesPath;
        this.imageClass = imageClass;
        this.maskPath = maskPath;
        initItems(limit);
    }

    public void initItems(int limit) {
        File[] files = new File(path).listFiles();
        int count = 0;
        for (File file : files) {
            if (file.isFile()) {
                if(limit > 0) {
                    if(count < limit) {
                        databaseItems.add(new DatabaseItem(path + file.getName(), file.getName(), imageClass, maskPath + file.getName()));
                        count++;
                    } else {
                        break;
                    }
                } else {
                    databaseItems.add(new DatabaseItem(path + file.getName(), file.getName(), imageClass, maskPath + file.getName()));
                }
            }
        }
    }

}
