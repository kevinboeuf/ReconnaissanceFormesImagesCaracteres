package com.company;

/**
 * Created by Kevin on 18/01/2016.
 */
public class Attribute<T> {
    public String name;
    public String type;
    public T value;

    public Attribute() {
    }

    public Attribute(String name, AttributeType type, T value) {
        this.name = name;
        this.type = type.type;
        this.value = value;
    }
}
