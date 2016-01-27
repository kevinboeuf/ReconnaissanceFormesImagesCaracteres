package com.company;

public class Pixel {

    int x = 0;
    int y = 0;

    public Pixel(int mx, int my) {
        x = mx;
        y = my;
    }

    public Pixel midPixel(Pixel pixel) {
        int mx = (pixel.x + x)/2;
        int my = (pixel.y + y)/2;
        return new Pixel(mx,my);
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
