package com.hackthon.WaitroseCrawler;




import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


//@SpringBootApplication
//@RestController
//public class DemoApplication {
//
//
//    public static void main(String[] args) {
//        SpringApplication.run(DemoApplication.class, args);
//    }
//
//    @GetMapping("/hello")
//    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
//        return String.format("Hello %s!", name);
//    }
//
//}


@Resource
class WebPageInfo {
    float totalPrice;
    List<Pair> itemList;
    WebPageInfo(float totalPrice, List<Pair> itemList){
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

    public void setItemList(List<Pair> itemList) {
        this.itemList = itemList;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}

@Resource
class Pair{
    String name;
    float price;
    float unit;
    Pair(String name, float price, float unit){
        this.name = name;
        this.price = price;
        this.unit = unit;
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

@SpringBootApplication
@RestController
public class WaitroseWebScrapper {
    WebClient webClient = new WebClient();
    String urlPrefix;
//    HtmlPage page;

    public WaitroseWebScrapper() throws IOException {
        urlPrefix = "https://www.waitrose.com/ecom/shop/search?&searchTerm=";
//        page = getWebPage(url);
    }

    private HtmlPage getWebPage(String url) throws IOException{
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient.getPage(url);
    }

    public Optional<Pair> searchItem(String str) throws IOException {
        HtmlPage page = getWebPage(urlPrefix+str);
//        page.save(new File("apple.html"));
        List<DomNode> list = page.getByXPath("//article[contains(@class, 'productPod___yz0mm')]");
        ArrayList<Pair> pairArrayList = new ArrayList<>();
//        int index = 0;
        for(DomNode ele : list){
            try{
                //get Name and Price
                String name = ele.getAttributes().getNamedItem("data-product-name").getNodeValue();
                String priceEle = ele.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(0).
                        getChildNodes().get(0).getChildNodes().get(1).getFirstChild().getFirstChild().toString();
                float price = 0;
                if(priceEle.contains("£")){
                    price = Float.parseFloat(priceEle.substring(1));
                }else if (priceEle.contains("p")){
                    price = Float.parseFloat("0."+priceEle.substring(0, priceEle.length()-1));
                }
//            System.out.println(index++);
                String unit = ele.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(0)
                        .getChildNodes().get(1).getChildNodes().get(1).toString();
                if(unit.contains("/kg")){
                    unit = unit.substring(0, unit.length() - "/kg".length());
                    float unit_price = 0;
                    if(unit.contains("£")){
                        unit_price = Float.parseFloat(unit.substring(1));
                    }else if (unit.contains("p")){
                        unit_price = Float.parseFloat("0."+unit.substring(0, unit.length()-1));
                    }
                    Pair pair = new Pair(name, price, unit_price);
                    pairArrayList.add(pair);
                }
            }catch (Exception e){
                e.printStackTrace();
            }



        }
        Optional<Pair> min = pairArrayList.stream().min(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return (int) (o1.unit- o2.unit);
            }
        });
//        System.out.println(min.get());
        return min;
    }
    @GetMapping("/getWaitrosePrice")
    @ResponseBody
    public WebPageInfo getTotalPrice(@RequestParam(value = "items") String items) throws IOException {
        String[] item_list = items.split(",");
        WaitroseWebScrapper webScrapper = new WaitroseWebScrapper();
        ArrayList<Pair> pairArrayList = new ArrayList<>();
        for(String item : item_list){
            Optional<Pair> ret = webScrapper.searchItem(item);
            if(!ret.isEmpty()){
                pairArrayList.add(ret.get());
            }
        }
        //return total price
        float sum = 0;
        for(Pair pair : pairArrayList){
            sum += pair.price;
        }
        DecimalFormat df = new DecimalFormat("0.00");

        WebPageInfo webPageInfo = new WebPageInfo(Float.parseFloat(df.format(sum)), pairArrayList);
        return webPageInfo;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WaitroseWebScrapper webScrapper = new WaitroseWebScrapper();
//        webScrapper.extractInformation();
        ArrayList<Pair> list = new ArrayList<>();
        Optional<Pair> item1 = webScrapper.searchItem("apple");
        Optional<Pair> item2 = webScrapper.searchItem("banana");
        Optional<Pair> item3 = webScrapper.searchItem("orange");
        if(!item1.isEmpty()){
            list.add(item1.get());
        }
        if(!item2.isEmpty()){
            list.add(item2.get());
        }
        if(!item3.isEmpty()){
            list.add(item3.get());
        }
        for(Pair ele : list){
            System.out.println();
        }
    }
}
