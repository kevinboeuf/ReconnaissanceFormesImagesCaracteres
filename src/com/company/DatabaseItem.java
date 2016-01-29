package com.company;

import com.company.model.ImageClass;

public class DatabaseItem {
    public String path = "";
    public String name = "";
    public ImageClass imageClass = ImageClass.ZERO;
    public String maskPath = "";

    public DatabaseItem(String path, String name, ImageClass imageClass, String maskPath) {
        this.path = path;
        this.name = name;
        this.imageClass = imageClass;
        this.maskPath = maskPath;
    }

    @Override
    public String toString() {
        return "DatabaseItem{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", imageClass=" + imageClass +
                ", maskPath='" + maskPath + '\'' +
                '}';
    }
}
