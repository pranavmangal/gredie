package com.hackthon;

import javax.annotation.Resource;
import java.util.List;

@Resource
public class WebPageInfo {
    String website;
    float totalPrice;
    List<Pair> itemList;

    public WebPageInfo(String website, float totalPrice, List<Pair> itemList) {
        this.website = website;
        this.totalPrice = totalPrice;
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "WebPageInfo{" +
                "totalPrice=" + totalPrice +
                ", itemList=" + itemList +
                '}';
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public List<Pair> getItemList() {
        return itemList;
    }

    public String getWebsite() {
        return website;
    }

    public void setItemList(List<Pair> itemList) {
        this.itemList = itemList;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
