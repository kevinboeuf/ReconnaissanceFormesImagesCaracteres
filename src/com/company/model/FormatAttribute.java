package com.company.model;

public enum FormatAttribute {
    HORIZONTAL_RECTANGLE("horizontal-rectangle"),
    VERTICAL_RECTANGLE("vertical-rectangle"),
    SQUARE("square");

    String name = "";

    FormatAttribute(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}