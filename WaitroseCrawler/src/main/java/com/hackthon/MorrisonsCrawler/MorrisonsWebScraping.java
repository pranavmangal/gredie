package com.hackthon.MorrisonsCrawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.hackthon.WaitroseCrawler.WaitroseWebScrapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


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
    public Ingredient findItem(String itemString) throws IOException {
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
        Ingredient newIngredient = new Ingredient(currentItemName,currentSmallestPrice);
        return newIngredient;
    }

    // Return total price of ingredients from this store
    public Float sumPrices(HashMap<String,Float> listOfIngredients){

        Float sum = 0f;
        for (Float val : listOfIngredients.values()){
            sum += val;
        }
        return sum;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getMorrisonsPrice")
    public float getTotalPrice(@RequestParam(value = "items") String items) throws IOException {

        // All ingredients
        HashMap<String,Float> listOfIngredients = new HashMap<>();

        // Initiate web scraper
        MorrisonsWebScraping webScraper = new MorrisonsWebScraping();
        String[] itemList = items.split(",");
        for(String item:itemList){
            Ingredient getIngredient = webScraper.findItem(item);
            // Add item to the ingredients list
            listOfIngredients.put(getIngredient.getName(), getIngredient.getPrice());
        }
        return webScraper.sumPrices(listOfIngredients);
    }

    public static void main(String[] args) throws IOException {

        // All ingredients
        HashMap<String,Float> listOfIngredients = new HashMap<>();

        // Initiate web scraper
        MorrisonsWebScraping webScraper = new MorrisonsWebScraping();
        String[] itemList = {"APPLE", "banana", "strawberry"};

        // Search for each item in the product list
        for(String item:itemList){
            Ingredient getIngredient = webScraper.findItem(item);
            // Add item to the ingredients list
            listOfIngredients.put(getIngredient.getName(), getIngredient.getPrice());
        }
        System.out.println(webScraper.sumPrices(listOfIngredients));
    }
}

