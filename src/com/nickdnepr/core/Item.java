package com.nickdnepr.core;

import java.util.HashMap;

public class Item {

    private HashMap<String, Double> attributes;
    private int classBelong;

    public Item(HashMap<String, Double> attributes, int classBelong) {
        this.attributes = attributes;
        this.classBelong = classBelong;
    }

    public HashMap<String, Double> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Double> attributes) {
        this.attributes = attributes;
    }

    public int getClassBelong() {
        return classBelong;
    }

    public void setClassBelong(int classBelong) {
        this.classBelong = classBelong;
    }

    @Override
    public String toString() {
        return "Item{" +
                "attributes=" + attributes +
                ", classBelong=" + classBelong +
                '}';
    }
}
