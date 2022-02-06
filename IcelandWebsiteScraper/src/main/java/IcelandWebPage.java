import java.util.List;

public class IcelandWebPage {
  float totalPrice;
  String nameWebsite;
  List<ItemDescription> listItems;

  IcelandWebPage(String webSiteName, float priceTotal, List<ItemDescription> listOfItems) {
    nameWebsite = webSiteName;
    totalPrice = priceTotal;
    listItems = listOfItems;
  }

  @Override
  public String toString() {

    return "WebPageInfo{" + "Total Price=" + totalPrice + ", Item List=" + listItems + "}";
  }

  public float getTotalPrice() {

    return totalPrice;
  }

  public List<ItemDescription> getListItems() {

    return listItems;
  }

  public String getNameWebsite() {

    return nameWebsite;
  }

  public void setListItems(List<ItemDescription> listItems) {

    this.listItems = listItems;
  }

  public void setTotalPrice(float totalPrice) {
    this.totalPrice = totalPrice;
  }

  public void setNameWebsite(String nameWebsite) {
    this.nameWebsite = nameWebsite;
  }
}
