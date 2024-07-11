/**
 * This class represents an item in a vending machine.
 * Each item has properties such as item name, price, and calories.
 * It provides methods to retrieve and modify these properties.
 */
public class Item {
    private String itemName;
    private int itemPrice;
    private int itemCalories;

    /**
     * this method sets the name of an item
     * @param itemName name of an item
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * this method sets the price of an item
     * @param itemPrice price of an item
     */
    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    /**
     * this method sets the calories of an item
     * @param itemCalories calories of an item
     */
    public void setItemCalories(int itemCalories) {
        this.itemCalories = itemCalories;
    }

    /**
     * This constructor method creates an item.
     * @param itemName name of an item
     * @param itemPrice price of an item
     * @param itemCalories calories of an item
     */
    public Item(String itemName, int itemPrice, int itemCalories) {
        setItemName(itemName);
        setItemPrice(itemPrice);
        setItemCalories(itemCalories);
    }
}
