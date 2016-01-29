package com.company;

import com.company.model.ImageClass;
import com.company.model.SDDImage;
import com.company.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DatabaseFolder {
    public String maskPath = "";
    public String path = "";
    public ImageClass imageClass = ImageClass.ZERO;
    ArrayList<DatabaseItem> databaseItems = new ArrayList<>();

    public DatabaseFolder(String imagesPath, ImageClass imageClass, String maskPath) {
        this.path = imagesPath;
        this.imageClass = imageClass;
        this.maskPath = maskPath;
        initItems();
    }

    public void initItems() {
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                databaseItems.add(new DatabaseItem(path + file.getName(), file.getName(), imageClass, maskPath + file.getName()));
            }
        }
    }

}
