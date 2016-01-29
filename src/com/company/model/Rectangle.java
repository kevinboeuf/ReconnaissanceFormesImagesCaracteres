package com.company.model;

import com.company.model.Pixel;

public class Rectangle {
    public Pixel top;
    public Pixel left;
    public Pixel bottom;
    public Pixel right;

    public int getWidth() {
        return Math.abs(right.x - left.x);
    }

    public int getHeight() {
        return Math.abs(bottom.y - top.y);
    }
}
