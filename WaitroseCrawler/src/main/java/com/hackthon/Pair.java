package com.hackthon;

import javax.annotation.Resource;

@Resource
public class Pair {
    String name;
    float price;
    float unit;
    String url;

    public Pair(String name, float price, float unit, String url) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.url = url;
<<<<<<< HEAD
    }

    public Pair(String name, float price) {
        this.name = name;
        this.price = price;
        this.unit = 0;
=======
>>>>>>> 0f29e24a0a65b84ea3caefddf956c1857bd945b4
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

    public String getUrl() {
        return url;
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

<<<<<<< HEAD
    public void setUrl(String url) {
        this.url = url;
    }
=======
    public String getUrl() {
        return url;
    }

    public void setUrl(String name) {
        this.url = url;
    }

>>>>>>> 0f29e24a0a65b84ea3caefddf956c1857bd945b4
}
