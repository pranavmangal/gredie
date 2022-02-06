package com.hackthon;

import javax.annotation.Resource;

@Resource
public class Pair {
    String name;
    float price;
    float unit;

    public Pair(String name, float price, float unit) {
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public Pair(String name, float price) {
        this.name = name;
        this.price = price;
        this.unit = 0;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", unit=" + unit +
                '}';
    }

    public float getPrice() {
        return price;
    }

    public float getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setUnit(float unit) {
        this.unit = unit;
    }

}
