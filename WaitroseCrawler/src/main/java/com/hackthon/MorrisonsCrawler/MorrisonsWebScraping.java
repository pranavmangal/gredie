package com.hackthon.MorrisonsCrawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import com.hackthon.WaitroseCrawler.WaitroseWebScrapper;

import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;

@Resource
class WebPageInfo {
    String website;
    float totalPrice;
    List<Ingredient> itemList;

    public WebPageInfo(String website, float totalPrice, List<Ingredient> itemList) {
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

    public List<Ingredient> getItemList() {
        return itemList;
    }

    public String getWebsite() {
        return website;
    }

    public void setItemList(List<Ingredient> itemList) {
        this.itemList = itemList;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

@RestController
public class MorrisonsWebScraping {
    HtmlPage page;
    WebClient webClient = new WebClient();

    public MorrisonsWebScraping() throws IOException {
        String morrisonsUrl = "https://groceries.morrisons.com/webshop/startWebshop.do";
        page = GetWebPageMorrisons(morrisonsUrl);
    }

    private HtmlPage GetWebPageMorrisons(String morrisonsUrl) throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        return webClient.getPage(morrisonsUrl);
    }

    // Returns the best ingredient for a given item
    public Pair findItem(String itemString) throws IOException {
        // Search for the item in the search bar
        itemString = itemString.toLowerCase(Locale.ROOT);
        DomElement inputField = page.getElementById("findText");
        DomElement submitButton = page.getElementById("findButton");
        inputField.setAttribute("value",itemString);
        HtmlPage page2 = submitButton.click();
        DomNode allRelevantItems = page2.getFirstByXPath("//ul[@class='fops fops-regular fops-shelf']");

        List<DomNode> allItems = allRelevantItems.getByXPath("//div[@class='fop-item']");
        int counter = 0;
        float currentSmallestPrice = Integer.MAX_VALUE;
        String currentItemName = "";

        // Get each card for an item
        for(DomNode item:allItems){
            DomElement itemNode = item.querySelector(".fop-title");
            DomElement itemPrice = item.querySelector(".fop-price");

            if(itemNode != null && itemPrice != null){
                String itemNameLabel = itemNode.getAttribute("title");
                itemNameLabel = itemNameLabel.toLowerCase(Locale.ROOT);
                String itemPriceLabel = itemPrice.getTextContent();

                if(itemNameLabel.contains(itemString)){
                    Float itemPriceFloat = 0f;
                    if(itemPriceLabel.charAt(0) == '£'){
                        itemPriceFloat = Float.parseFloat(itemPriceLabel.substring(1));
                    }else{
                        itemPriceFloat = Float.parseFloat(itemPriceLabel.substring(0, itemPriceLabel.length() - 1))/100;
                    }
                    if(counter <3){
                        counter++;
                        if(itemPriceFloat<currentSmallestPrice){
                            currentSmallestPrice = itemPriceFloat;
                            currentItemName = itemNameLabel;
                        }
                        //System.out.println(itemNameLabel);
                        //System.out.println(Float.toString(itemPriceFloat));
                    }else{
                        break;
                    }
                }
            }
        }
        System.out.println("Best item: "+currentItemName);
        Ingredient newIngredient = new Pair(currentItemName,currentSmallestPrice);
        return newIngredient;
    }

    // Return total price of ingredients from this store
    public Float sumPrices(ArrayList<Pair> listOfIngredients){

        Float sum = 0f;
        for (Pair ingredient : listOfIngredients){
            sum += ingredient.getPrice();
        }
        return sum;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getMorrisonsPrice")
    @ResponseBody
    public WebPageInfo getTotalPrice(@RequestParam(value = "items") String items) throws IOException {

        // All ingredients
        HashMap<String,Float> listOfIngredients = new HashMap<>();

        // Initiate web scraper
        MorrisonsWebScraping webScraper = new MorrisonsWebScraping();
        String[] itemList = items.split(",");
        ArrayList<Ingredient> retList = new ArrayList<>();
        for(String item:itemList){
            Ingredient getIngredient = webScraper.findItem(item);
            // Add item to the ingredients list
            listOfIngredients.put(getIngredient.getName(), getIngredient.getPrice());
            retList.add(getIngredient);
        }
//        return webScraper.sumPrices(listOfIngredients);
        WebPageInfo webPageInfo = new WebPageInfo("Morrisons",webScraper.sumPrices(listOfIngredients), retList);
        return webPageInfo;
    }

    public static void main(String[] args) throws IOException {

        // All ingredients
        Arraylist<Pair> listOfIngredients = new HashMap<>();

        // Initiate web scraper
        MorrisonsWebScraping webScraper = new MorrisonsWebScraping();
        String[] itemList = {"APPLE", "banana", "strawberry"};

        // Search for each item in the product list
        for(String item:itemList){
            Pair getIngredient = webScraper.findItem(item);
            // Add item to the ingredients list
            listOfIngredients.add(getIngredient);
        }
        System.out.println(webScraper.sumPrices(listOfIngredients));
    }
}

