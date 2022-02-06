public class ItemDescription {
  String itemName;
  Float itemPrice;

  ItemDescription(String name, Float price) {
    itemName = name;
    itemPrice = price;
  }

  @Override
  public String toString() {
    return itemName + " with the price of £" + itemPrice;
  }
}
