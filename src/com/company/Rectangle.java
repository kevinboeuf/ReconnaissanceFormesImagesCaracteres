package com.company;

public class Rectangle {
    Pixel top;
    Pixel left;
    Pixel bottom;
    Pixel right;

    public int getWidth() {
        return right.x - left.x;
    }

    public int getHeight() {
        return bottom.y - top.y;
    }
}
