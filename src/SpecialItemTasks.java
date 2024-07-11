/**
 * This class represents the preparation process of special items.
 * Each special item has properties such as item name, price, and calories.
 * It provides methods to retrieve the String depicting these processes.
 */
public class SpecialItemTasks {
    private final String[] specialNames = {"Coke Float", "Ultimate Burger Steak Meal", "Burger Bundle",
            "Chicken Joy Bucket Meal", "Kiddie Meal 1", "Kiddie Meal 2", "Super Meal 1",
            "Super Meal 2", "Super Meal 3"};
    private final String[] cokeFloatTasks = {"Dispensing Ice...", "Pouring Coca Cola...", "Dispensing Ice Cream...",
            "Pouring Chocolate Syrup...", "Placing Straw in Cup..."};
    private final String[] ultBurgStkTasks = {"Frying Burger Patty...", "Frying French Fries...",
            "Pouring Mushroom Gravy...", "Scooping Plain Rice...", "Pouring Coca Cola..."};
    private final String[] burgBundleTasks = {"Frying Burger Patties...", "Toasting Burger Buns...",
            "Spreading Special Sauce...", "Pouring Coca Cola...", "Frying French Fries..."};
    private final String[] chixBucketTasks = {"Frying Chicken Joy...", "Frying Peach Mango Pies...",
            "Scooping Plain Rice...", "Dispensing Ice...", "Pouring Coca Cola..."};
    private final String[] kidMeal1Tasks = {"Frying Burger Patty...", "Toasting Burger Buns...",
            "Spreading Special Sauce...", "Dispensing Ice...", "Pouring Coca Cola..."};
    private final String[] kidMeal2Tasks = {"Boiling Pasta Noodles...", "Frying Hotdogs...",
            "Mixing In Spaghetti Sauce...", "Dispensing Ice...", "Pouring Coca Cola..."};
    private final String[] superMeal1Tasks = {"Frying Chicken Joy...", "Frying Burger Patty...",
            "Boiling Pasta Noodles...", "Mixing In Spaghetti Sauce...", "Pouring Coca Cola..."};
    private final String[] superMeal2Tasks = {"Frying Chicken Joy...", "Frying Peach Mango Pies...",
            "Boiling Pasta Noodles...", "Mixing In Spaghetti Sauce...", "Pouring Coca Cola..."};
    private final String[] superMeal3Tasks = {"Frying Burger Patty...", "Frying French Fries...",
            "Boiling Pasta Noodles...", "Mixing In Spaghetti Sauce...", "Pouring Coca Cola..."};
    private final String[][] specialTasks = {cokeFloatTasks, ultBurgStkTasks, burgBundleTasks, chixBucketTasks, kidMeal1Tasks,
            kidMeal2Tasks, superMeal1Tasks, superMeal2Tasks, superMeal3Tasks};

    /**
     * this method gets the names of special items
     * @return array containing names of special items
     */
    public String[] getSpecialNames() {
        return specialNames;
    }

    /**
     * this method gets the processes of preparing the special items to be dispensed
     * @return array containing arrays of String referring to special item preparation
     */
    public String[][] getSpecialTasks() {
        return specialTasks;
    }

    /**
     * This is the only constructor method in this class, and it creates the instance of SpecialItemTasks.
     * This constructor is an "empty constructor".
     */
    public SpecialItemTasks() {

    }
}
