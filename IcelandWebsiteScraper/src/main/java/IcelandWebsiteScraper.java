import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IcelandWebsiteScraper {

  HtmlPage page;
  WebClient webClient = new WebClient();

  String icelandUrlPrefix = "https://www.iceland.co.uk/search?q=";
  String icelandUrlSuffix = "&lang=default";

  private HtmlPage getWebPageIceland(String icelandUrl) throws IOException {
    webClient.getOptions().setJavaScriptEnabled(false);
    webClient.getOptions().setCssEnabled(false);

    return webClient.getPage(icelandUrl);
  }

  public ItemDescription extractInfo(String args) throws IOException {
    String url = icelandUrlPrefix + args + icelandUrlSuffix;
    page = getWebPageIceland(url);
    List<DomText> searchedItems = page.getByXPath("//span/text()");

    List<Float> results = new ArrayList<>();

    String regex = "(£[0-9]+.[0-9]*)";
    for (DomText domText : searchedItems) {
      String txt = domText.toString();
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(txt);
      if (m.find() && (!m.group(0).equals("£0.00"))) {
        results.add(Float.valueOf(m.group(0).substring(1)));
      }
    }

    Optional<Float> min = results.stream().min(new Comparator<Float>() {
      @Override
      public int compare(Float o1, Float o2) {
        if (o1 < o2) {
          return -1;
        } else if (o1 == o2) {
          return 0;
        } else {
          return 1;
        }
      }
    });

    if (min.isPresent()) {
      return new ItemDescription("Iceland " + args, min.get());
    } else {
      return new ItemDescription("No item found from Iceland's website", (float) 0);
    }
  }

  public static void main(String[] args) throws IOException {
    IcelandWebsiteScraper icelandWebsiteScraper = new IcelandWebsiteScraper();
    System.out.println(icelandWebsiteScraper.extractInfo(args[0]));
  }
}
