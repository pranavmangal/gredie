package com.hackthon.MorrisonsCrawler;

import javax.annotation.Resource;
import java.io.IOException;

@Resource
public class Ingredient {
    String name;
    Float price;

    public Ingredient(String name,Float price) {
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public Float getPrice(){
        return this.price;
    }
}
