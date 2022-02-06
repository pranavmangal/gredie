import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
// ınstall jsoup
// can take ın the html addr as a strıng, then search Elements

public class SainsburrysWebsiteScraper {

  HtmlPage page;
  WebClient webClient = new WebClient();

  List<DomText> searchedItems = new ArrayList<>();

  public SainsburrysWebsiteScraper() throws IOException {
    String sainsburrysUrl = "https://www.iceland.co.uk/search?q=apple&lang=default";
    page = GetWebPageSainsburrys(sainsburrysUrl);


  }

  private HtmlPage GetWebPageSainsburrys(String sainsburrysUrl) throws IOException {
    webClient.getOptions().setJavaScriptEnabled(false);
    webClient.getOptions().setCssEnabled(false);

    return webClient.getPage(sainsburrysUrl);
  }

  public String extractInfo() {

    List<DomText> searchedItems = page.getByXPath("//span/text()");


    List<String> itemTitles = new ArrayList<>();

    for (DomText domText: searchedItems) {
      String txt = domText.toString();
      itemTitles.add(txt);

    }

    String regex = "£[0-9].[0-9]*";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(itemTitles.toString());

    return m.toString();
  }

  public static void main(String[] args) throws IOException {
    SainsburrysWebsiteScraper sainsburrysWebsiteScraper = new SainsburrysWebsiteScraper();
    System.out.println(sainsburrysWebsiteScraper.extractInfo());

  }

}
