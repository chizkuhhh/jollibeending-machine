import java.util.ArrayList;

/**
 * This class represents a slot in a vending machine that holds an amount of items.
 * Each slot contains information such as the item name, stock count, item price, item calories, and sold count.
 * It also provides methods to retrieve and modify these values.
 */
public class Slot {
    private String slotItem;
    private int slotPrice;
    private int slotStock;
    private int slotCals;
    private int slotSold;

    private ArrayList<Item> items;

    /**
     * This method gets the name of an item in a slot.
     * @return item name
     */
    public String getSlotItem() {
        return slotItem;
    }

    /**
     * This method gets the price of a specific item.
     * @return item price
     */
    public int getSlotPrice() {
        return slotPrice;
    }

    /**
     * This method gets the amount of a specific item available.
     * @return item count
     */
    public int getSlotStock() {
        return slotStock;
    }

    /**
     * This method gets the amount of calories of a specific item.
     * @return item calories
     */
    public int getSlotCals() {
        return slotCals;
    }

    /**
     * This method gets the amount of stock of an item subtracted from the initial among of stock.
     * @return item stock sold
     */
    public int getSlotSold() {
        return slotSold;
    }

    /**
     * this method gets the list of item names
     * @return array containing item names
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * This method sets the name of a specific item.
     * @param slotItem item name
     */
    public void setSlotItem(String slotItem) {
        this.slotItem = slotItem;
    }

    /**
     * This method sets the price of a specific item.
     * @param slotPrice item price
     */
    public void setSlotPrice(int slotPrice) {
        this.slotPrice = slotPrice;
    }

    /**
     * This method sets the amount of a specific item available.
     * @param slotStock item count
     */
    public void setSlotStock(int slotStock) {
        this.slotStock = slotStock;
    }

    /**
     * This method sets the amount of calories of a specific item.
     * @param slotCals item calories
     */
    public void setSlotCals(int slotCals) {
        this.slotCals = slotCals;
    }

    /**
     * This method sets the amount of stock of an item subtracted from the initial among of stock.
     * @param slotSold item stock sold
     */
    public void setSlotSold(int slotSold) {
        this.slotSold = slotSold;
    }

    /**
     * This constructor method sets the information of each item slot to be empty upon
     * initial instantiation of a vending machine.
     */
    public Slot() {
        setSlotItem("EMPTY");
        setSlotPrice(0);
        setSlotStock(10);
        setSlotCals(0);
        setSlotSold(0);
        items = new ArrayList<>();
    }
}
