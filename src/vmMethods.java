/**
 * This class represents some of the methods used by the vending machine.
 * It checks the stock of money denominations stored by a vending machine to see if there is enough to produce change.
 * It also provides the item names of regular items.
 */
public class vmMethods {

    /**
     * this method checks if a regular vending machine can produce sufficient change
     * @param totalPayment operator inputted money
     * @param vmSlots array of slots in the vending machine
     * @param slotNum slot array index number
     * @param money instance of Money class
     * @return availability status of sufficient change
     */
    public boolean checkEnoughChange(int totalPayment, Slot[] vmSlots, int slotNum, Money money) {
        int change = totalPayment - vmSlots[slotNum - 1].getSlotPrice();
        int[] billDenominations = {1000, 500, 200, 100, 50, 20, 10, 5, 1};
        int[] billCounter = new int[9];
        int[] stock = money.getDenominationStock();

        for (int i = 0; i < 9; i++) {
            // get the count for each bill in the change
            if (change >= billDenominations[i]) {
                billCounter[i] = change / billDenominations[i];
                change = change % billDenominations[i];

                if (billCounter[i] >= stock[i])
                    return false;

            }
        }
        return true;
    }

    /**
     * this method checks if a special vending machine can produce sufficient change
     * @param isSpecial special item status
     * @param totalPayment operator inputted money
     * @param specialPrice special item price
     * @param vmSlots array of slots in the vending machine
     * @param slotNum slot array index number
     * @param money instance of Money class
     * @return availability status of sufficient change
     */
    public boolean checkEnoughChangeSpecial(boolean isSpecial, int totalPayment, int specialPrice
            , Slot[] vmSlots, int slotNum, Money money) {
        int change;
        if (isSpecial) {
            change = totalPayment - specialPrice;
        } else {
            change = totalPayment - vmSlots[slotNum].getSlotPrice();
        }

        int[] billDenominations = {1000, 500, 200, 100, 50, 20, 10, 5, 1};
        int[] billCounter = new int[9];
        int[] stock = money.getDenominationStock();

        for (int i = 0; i < 9; i++) {
            // get the count for each bill in the change
            if (change >= billDenominations[i]) {
                billCounter[i] = change / billDenominations[i];
                change = change % billDenominations[i];

                if (billCounter[i] >= stock[i])
                    return false;
            }
        }
        return true;
    }

    /**
     * this method instantiates slots for the vending machine and
     * sets their specifications
     * @return array of slots
     */
    public Slot[] initSlots() {
        Slot slot1 = new Slot();
        Slot slot2 = new Slot();
        Slot slot3 = new Slot();
        Slot slot4 = new Slot();
        Slot slot5 = new Slot();
        Slot slot6 = new Slot();
        Slot slot7 = new Slot();
        Slot slot8 = new Slot();

        Slot[] slotsTemp = {slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8};
        String[] items = {"Ice Cream Sundae", "Coca Cola", "Burger Steak", "Chicken Joy", "Jolly Spaghetti",
                "Yumburger", "Jolly Crispy Fries", "Peach Mango Pie"};
        int[] prices = {48, 53, 59, 82, 59, 40, 48, 48};
        int[] calories = {270, 160, 305, 320, 305, 250, 340, 120};

        for (int i = 0; i < slotsTemp.length; i++) {
            slotsTemp[i].setSlotItem(items[i]);
            slotsTemp[i].setSlotPrice((prices[i]));
            slotsTemp[i].setSlotCals(calories[i]);

            // add actual instances of Item class in each slot
            for (int j = 0; j < 10; j++) {
                slotsTemp[i].getItems().add(new Item(items[i], prices[i], calories[i]));
            }
        }

        return slotsTemp;
    }

    /**
     * This is the only constructor method in this class, and it creates the instance of vmMethods.
     * Seeing as this class only contains methods, it's constructor is an "empty constructor".
     */
    public vmMethods() {

    }
}
