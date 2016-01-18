package com.company;

/**
 * Created by Kevin on 18/01/2016.
 */
public enum AttributeType {
    INTEGER("NUMERIC"),
    STRING("STRING");


    String type;

    AttributeType(String type) {
        this.type = type;
    }
}
