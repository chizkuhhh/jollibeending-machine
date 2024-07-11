import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

/**
 * This class represents a special vending machine, which is capable of storing at least 10 items in its
 * 8 slots with an additional 3 slots also capable of storing at least 10 add-on items.
 * It allows user to select a denomination of money to insert into the vending machine,
 * produces change in denominations for the user, allows them to select an item of their choice to purchase,
 * allows them to create their own combination of the set items called "specialized items",
 * and displays all relevant information such as the item's type, price, stock, and total calories.
 */
public class SpecialVM extends JFrame {
    private JTextPane item1;
    private JTextPane item5;
    private JTextPane item7;
    private JTextPane item2;
    private JTextPane item3;
    private JTextPane item4;
    private JTextPane item6;
    private JTextPane item8;

    private final JTextPane[] slotDisplay = {item1, item2, item3, item4, item5, item6, item7, item8};
    private JTextPane processDisplay;
    private JComboBox<String> insertPayment;
    private JButton dispenseButton;
    private JButton cancelButton;
    private JButton insertButton;
    private JPanel specialVM;
    private JButton backButton;
    private JSpinner slot1;
    private JSpinner slot3;
    private JSpinner slot5;
    private JSpinner slot7;
    private JSpinner slot2;
    private JSpinner slot4;
    private JSpinner slot6;
    private JSpinner slot8;
    private JRadioButton extraRiceRadioButton;
    private JRadioButton gravyRadioButton;
    private JRadioButton ketchupRadioButton;
    private JButton confirmButton;
    private JScrollPane scrollContent;
    private JLabel ketchupPic;
    private JLabel gravyPic;
    private JLabel ricePic;
    private JScrollPane displayScroll;
    private JRadioButton[] addOns = {extraRiceRadioButton, gravyRadioButton, ketchupRadioButton};
    private JSpinner[] slotSpinners = {slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8};
    private int totalPayment;
    private int slotNum;
    private Money money = new Money();

    private Slot[] vmSlots;
    private boolean isSpecial;
    private boolean isConfirmed;
    private int specialPrice;
    private String specialName;
    private String specialImg;
    private Slot[] addOnInfo;
    private String addOnNames;
    private int addOnPrices;
    private int addOnCals;
    private int totalSales;

    /**
     * This method gets the array of slots in the vending machine.
     * @return array of slots in the vending machine
     */
    public Slot[] getVmSlots() {
        return vmSlots;
    }

    /**
     * This method gets the total amount of money stored in a vending machine.
     * @return money stored in the vending machine
     */
    public Money getMoney() {
        return money;
    }

    /**
     * This method gets the total amount of money a user spent on items from the vending machine.
     * @return money earned by the vending machine
     */
    public int getTotalSales() {
        return totalSales;
    }

    /**
     * this method gets the information related to add-on items
     * @return information related to add-on items
     */
    public Slot[] getAddOnInfo() {
        return addOnInfo;
    }

    /**
     * This method sets the total amount of money a user spent on items from the vending machine.
     * @param totalSales money earned by the vending machine
     */
    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * This is the only constructor method in this class, and it creates the instance of a special vending machine.
     * @param sourceFrame is an instance of the TestVM class
     */
    public SpecialVM(TestVM sourceFrame) {
        vmMethods meth = new vmMethods();

        vmSlots = meth.initSlots();
        initTextPanes();
        disableSpinners();
        dispenseButton.setEnabled(false);
        addOnInfo = initAddOns();
        addOnPrices = 0;
        addOnCals = 0;
        addOnNames = "\nAdditional:\n";
        isConfirmed = false;
        totalSales = 0;

        updateProcessDisplay("Insert Money to Begin...");

        setContentPane(specialVM);
        setTitle("Special Vending Machine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 816);
        setLocationRelativeTo(null);
        setResizable(false);

        // faster scrolling
        scrollContent.getVerticalScrollBar().setUnitIncrement(13);

        // change scrollbar color
        scrollContent.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.decode("#DC0000");
            }
        });
        scrollContent.getVerticalScrollBar().getComponent(0).setBackground(Color.decode("#FEAF17"));
        scrollContent.getVerticalScrollBar().getComponent(1).setBackground(Color.decode("#FEAF17"));

        displayScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.decode("#DC0000");
            }
        });
        displayScroll.getVerticalScrollBar().getComponent(0).setBackground(Color.decode("#FEAF17"));
        displayScroll.getVerticalScrollBar().getComponent(1).setBackground(Color.decode("#FEAF17"));

        for (int i = 0; i < money.getDenominations().length; i++) {
            insertPayment.addItem(money.getDenominations()[i]);
        }
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                sourceFrame.setVisible(true);
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPayment();
                enableSpinners();
            }
        });
        dispenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (meth.checkEnoughChangeSpecial(isSpecial, totalPayment, specialPrice, vmSlots, slotNum, money)) {
                    if (dispenseItem()) {
                        if (isSpecial) {
                            String breakdown = produceChange();
                            new LoadingScreen(specialName, breakdown, specialImg);
                            totalSales += specialPrice;
                        } else {
                            produceChange();
                        }
                        totalPayment = 0;
                    } else {
                        updateProcessDisplay("Returning money...");
                        StringBuilder breakdown = new StringBuilder();
                        for (int i = 0; i < 9; i++) {
                            if (money.getPaymentBreakdown()[i] != 0) {
                                breakdown.append(money.getDenominations()[i]).append(": ").
                                        append(money.getPaymentBreakdown()[i]).append(" pc/s\n");
                            }
                        }

                        JOptionPane.showMessageDialog(specialVM, "Your change is:\n" + breakdown);
                    }
                    initTextPanes();
                    updateAddOnDisplay();
                    resetSpinners();
                } else {
                    JOptionPane.showMessageDialog(specialVM, "Machine low on change!");
                    updateProcessDisplay("Returning money...");
                    StringBuilder breakdown = new StringBuilder();
                    for (int i = 0; i < 9; i++) {
                        if (money.getPaymentBreakdown()[i] != 0) {
                            breakdown.append(money.getDenominations()[i]).append(": ").
                                    append(money.getPaymentBreakdown()[i]).append(" pc/s\n");
                        }
                    }

                    JOptionPane.showMessageDialog(specialVM, "Your change is:\n" + breakdown);
                }

                money.zeroPaymentBreakdown();
                disableSpinners();
                dispenseButton.setEnabled(false);
            }
        });

        // assign the same action listener to all slot spinners
        for (int i = 0; i < 8; i++) {
            slotSpinners[i].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    selectSpecialized();
                }
            });
        }

        // assign action listener to all add-on radio buttons
        for (int i = 0; i < 3; i++) {
            int j = i;
            addOns[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (addOns[j].isSelected() && !(addOnNames.contains(addOnInfo[j].getSlotItem()))) {
                        addOnPrices += addOnInfo[j].getSlotPrice();
                        addOnCals += addOnInfo[j].getSlotCals();
                        addOnNames += addOnInfo[j].getSlotItem() + "\n";

                        confirmButton.setEnabled(true);
                    } else if (!addOns[j].isSelected() && addOnNames.contains(addOnInfo[j].getSlotItem())) {
                        // should remove the add-on
                        addOnNames = addOnNames.replace(addOnInfo[j].getSlotItem() + "\n", "");
                        addOnPrices -= addOnInfo[j].getSlotPrice();
                        addOnCals -= addOnInfo[j].getSlotCals();

                        if (!isChecked()) {
                            confirmButton.setEnabled(false);
                        }
                    }
                }
            });
        }

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specialName += addOnNames;
                specialPrice += addOnPrices;

                updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                ".00\n You have selected items to dispense " + specialName + "\n" +
                        "Price: PHP " + specialPrice + ".00\nCalories: " + (getTotalCals() + addOnCals));

                extraRiceRadioButton.setEnabled(false);
                gravyRadioButton.setEnabled(false);
                ketchupRadioButton.setEnabled(false);
                confirmButton.setEnabled(false);
                isConfirmed = true;
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProcessDisplay("Returning money...");
                StringBuilder breakdown = new StringBuilder();
                for (int i = 0; i < 9; i++) {
                    if (money.getPaymentBreakdown()[i] != 0) {
                        breakdown.append(money.getDenominations()[i]).append(": ").
                                append(money.getPaymentBreakdown()[i]).append(" pc/s\n");
                    }
                }

                JOptionPane.showMessageDialog(specialVM, "Your change is:\n" + breakdown);
                totalPayment = 0;
                money.zeroPaymentBreakdown();
                disableSpinners();
                dispenseButton.setEnabled(false);
                resetSpinners();
            }
        });
    }

    /**
     * This method updates the content of the text pane to describe an ongoing process
     * @param text is the String found in the text pane
     */
    public void updateProcessDisplay(String text) {
        // align text to center
        StyledDocument style = processDisplay.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
        style.setParagraphAttributes(0, style.getLength(), align, false);

        // assign text
        processDisplay.setText(text);
        processDisplay.setEditable(false);
    }

    /**
     * This method initializes the text panes found in the GUI frame corresponding to
     * the test vending feature function of the program
     */
    public void initTextPanes() {
        for (int i = 0; i < slotDisplay.length; i++) {
            // align text to center
            StyledDocument style = slotDisplay[i].getStyledDocument();
            SimpleAttributeSet align = new SimpleAttributeSet();
            StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
            style.setParagraphAttributes(0, style.getLength(), align, false);

            // assign contents of text pane
            slotDisplay[i].setText(vmSlots[i].getSlotItem() + "\nPHP " + vmSlots[i].getSlotPrice() + ".00\n" + "(" +
                    vmSlots[i].getSlotStock() + " pcs available)\n" + vmSlots[i].getSlotCals() + " calories");
            slotDisplay[i].setEditable(false);
        }
    }

    /**
     * This method inserts a specific denomination into the vending machine as payment
     */
    public void addPayment() {
        int nInd = insertPayment.getSelectedIndex();
        int[] denomination = {1000, 500, 200, 100, 50, 20, 10, 5, 1};
        totalPayment += denomination[nInd];
        money.addDenomCount(nInd);
        updateProcessDisplay(insertPayment.getSelectedItem() + " inserted\n" + "Total Amount Inserted: PHP "
                + totalPayment + ".00");
    }

    /**
     * this method instantiates JSpinners for each slot to select the amount of an item
     * to be dispensed or used in creating a special item
     */
    private void createUIComponents() {
        // sets the minimum amt for each spinner to 0 and the maximum value to 10 (initial stock)
        slot1 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        slot2 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        slot3 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        slot4 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        slot5 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        slot6 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        slot7 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        slot8 = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
    }

    /**
     * this method initializes information related to special items and add-ons
     */
    public void selectSpecialized() {
        int iceCreamAmt = (Integer) slot1.getValue();
        int cokeAmt = (Integer) slot2.getValue();
        int burgerSteakAmt = (Integer) slot3.getValue();
        int chickenJoyAmt = (Integer) slot4.getValue();
        int spagAmt = (Integer) slot5.getValue();
        int yumburgAmt = (Integer) slot6.getValue();
        int friesAmt = (Integer) slot7.getValue();
        int peachMangoAmt = (Integer) slot8.getValue();

        // Add-Ons
        // ketchup - avail for fries
        // plain rice - avail for chicken, burger steak
        // gravy - avail for chicken, burger steak

        if (cokeAmt == 1 && iceCreamAmt == 1 && burgerSteakAmt == 0 && chickenJoyAmt == 0
                && spagAmt == 0 && yumburgAmt == 0 && friesAmt == 0 && peachMangoAmt == 0) {
            // Coke Float: Coca Cola + Ice Cream Sundae
            specialPrice = 67;
            isSpecial = true;
            specialName = "Coke Float ";
            specialImg = "food_images/cokefloat.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Coke Float!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (burgerSteakAmt == 1 && friesAmt == 1 && cokeAmt == 1 && iceCreamAmt == 0 && chickenJoyAmt == 0
                && spagAmt == 0 && yumburgAmt == 0 && peachMangoAmt == 0) {
            // Ultimate Burger Steak Meal: Burger Steak + Jolly Crispy Fries + Coca-Cola
            if (addOnInfo[0].getSlotStock() > 0)
                extraRiceRadioButton.setEnabled(true);
            if (addOnInfo[1].getSlotStock() > 0)
                gravyRadioButton.setEnabled(true);
            if (addOnInfo[2].getSlotStock() > 0)
                ketchupRadioButton.setEnabled(true);
            specialPrice = 170;
            isSpecial = true;
            specialName = "Ultimate Burger Steak Meal ";
            specialImg = "food_images/ultiburgerstk.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Ultimate Burger Steak Meal!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (yumburgAmt == 3 && cokeAmt == 3 && friesAmt == 3 && iceCreamAmt == 0 && burgerSteakAmt == 0
                && chickenJoyAmt == 0 && spagAmt == 0 && peachMangoAmt == 0) {
            // Burger Bundle: 3pcs Yumburger + 3pcs Coca-Cola + 3pcs Jolly Crispy Fries
            if (addOnInfo[2].getSlotStock() > 0)
                ketchupRadioButton.setEnabled(true);
            specialPrice = 195;
            isSpecial = true;
            specialName = "Burger Bundle ";
            specialImg = "food_images/burgbundle.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Burger Bundle!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (chickenJoyAmt == 6 && peachMangoAmt == 3 && cokeAmt == 3 && iceCreamAmt == 0
                && burgerSteakAmt == 0 && spagAmt == 0 && yumburgAmt == 0 && friesAmt == 0) {
            // Chicken Joy Bucket Meal: 6pcs Chicken Joy + 3pcs Peach Mango Pie + 3pcs Coca-Cola
            if (addOnInfo[0].getSlotStock() > 0)
                extraRiceRadioButton.setEnabled(true);
            if (addOnInfo[1].getSlotStock() > 0)
                gravyRadioButton.setEnabled(true);
            specialPrice = 796;
            isSpecial = true;
            specialName = "Chicken Joy Bucket Meal ";
            specialImg = "food_images/bucketmeal.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Chicken Joy Bucket Meal!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (yumburgAmt == 1 && cokeAmt == 1 && friesAmt == 0 && spagAmt == 0 && iceCreamAmt == 0
                && burgerSteakAmt == 0 && chickenJoyAmt == 0 && peachMangoAmt == 0) {
            // Kiddie Meal 1: Yumburger & Coca Cola
            specialPrice = 106;
            isSpecial = true;
            specialName = "Kiddie Meal 1 ";
            specialImg = "food_images/kiddie1.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Kiddie Meal 1\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (spagAmt == 1 && cokeAmt == 1 && chickenJoyAmt == 0 && burgerSteakAmt == 0 && iceCreamAmt == 0
                && yumburgAmt == 0 && friesAmt == 0 && peachMangoAmt == 0) {
            // Kiddie Meal 2: Spaghetti & Coca Cola
            specialPrice = 106;
            isSpecial = true;
            specialName = "Kiddie Meal 2 ";
            specialImg = "food_images/kiddie2.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Kiddie Meal 2!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (chickenJoyAmt == 1 && burgerSteakAmt == 1 && spagAmt == 1 && cokeAmt == 1 && iceCreamAmt == 0
                && yumburgAmt == 0 && friesAmt == 0 && peachMangoAmt == 0) {
            // Super Meal 1: 1pc Chicken Joy + Burger Steak + Jolly Spaghetti + Coca-Cola
            if (addOnInfo[0].getSlotStock() > 0)
                extraRiceRadioButton.setEnabled(true);
            if (addOnInfo[1].getSlotStock() > 0)
                gravyRadioButton.setEnabled(true);
            specialPrice = 221;
            isSpecial = true;
            specialName = "Super Meal 1 ";
            specialImg = "food_images/super1.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Super Meal 1!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (chickenJoyAmt == 1 && spagAmt == 1 && peachMangoAmt == 1 && cokeAmt == 1 && burgerSteakAmt == 0
                && friesAmt == 0 && yumburgAmt == 0 && iceCreamAmt == 0) {
            // Super Meal 2: 1pc Chicken Joy + Jolly Spaghetti + Peach Mango Pie + Coca-Cola
            if (addOnInfo[0].getSlotStock() > 0)
                extraRiceRadioButton.setEnabled(true);
            if (addOnInfo[1].getSlotStock() > 0)
                gravyRadioButton.setEnabled(true);
            specialPrice = 221;
            isSpecial = true;
            specialName = "Super Meal 2 ";
            specialImg = "food_images/super2.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Super Meal 2!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (yumburgAmt == 1 && friesAmt == 1 && spagAmt == 1 && cokeAmt == 1 && chickenJoyAmt == 0
                && burgerSteakAmt == 0 && iceCreamAmt == 0 && peachMangoAmt == 0) {
            // Super Meal 3: Yumburger + Jolly Crispy Fries + Jolly Spaghetti + Coca-Cola
            if (addOnInfo[2].getSlotStock() > 0)
                ketchupRadioButton.setEnabled(true);
            specialPrice = 179;
            isSpecial = true;
            specialName = "Super Meal 3 ";
            specialImg = "food_images/special3.png";
            dispenseButton.setEnabled(true);
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                    ".00\n You have selected items to dispense Super Meal 3!\n" +
                    "Price: PHP " + specialPrice + ".00\nCalories: " + getTotalCals());
        } else if (getSelectedSlot() >= 0){
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment + ".00\n"
                    + vmSlots[getSelectedSlot()].getSlotItem() + " Selected");
            extraRiceRadioButton.setEnabled(false);
            gravyRadioButton.setEnabled(false);
            ketchupRadioButton.setEnabled(false);
            isSpecial = false;
            specialPrice = 0;
            dispenseButton.setEnabled(true);
        } else {
            updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment + ".00\n");
            extraRiceRadioButton.setEnabled(false);
            gravyRadioButton.setEnabled(false);
            isSpecial = false;
            specialPrice = 0;
            dispenseButton.setEnabled(false);
        }
    }

    /**
     * This method displays a specific message depending on the success or failure of
     * item dispensing and updates stock if dispensing is successful
     * @return true or false for item dispensing status
     */
    public boolean dispenseItem() {
        int spinCounter = 0, nInd = -1;
        boolean notOne = false;
        if (isSpecial && totalPayment >= specialPrice) {
            for (int i = 0; i < 8; i++) {
                if ((Integer) slotSpinners[i].getValue() >= 1) {
                    // subtract from stock of each item
                    vmSlots[i].setSlotStock(vmSlots[i].getSlotStock() - (Integer) slotSpinners[i].getValue());

                    // add to slot sold of each item
                    vmSlots[i].setSlotSold(vmSlots[i].getSlotSold() + (Integer) slotSpinners[i].getValue());

                    // remove <spinner value> amount of elements from the array of items
                    for (int j = 0; j < (Integer) slotSpinners[i].getValue(); j++) {
                        vmSlots[i].getItems().remove(vmSlots[i].getItems().size() - 1);

                        slotSpinners[i].setModel(new SpinnerNumberModel(0, 0,
                                vmSlots[i].getSlotStock(), 1));
                    }
                }
            }

            if (addOnNames.contains("Extra Rice")) {
                if(isConfirmed) {
                    addOnInfo[0].setSlotStock(addOnInfo[0].getSlotStock() - 1);
                    addOnInfo[0].setSlotSold(addOnInfo[0].getSlotSold() + 1);
                }
            }

            if (addOnNames.contains("Gravy")) {
                if(isConfirmed) {
                    addOnInfo[1].setSlotStock(addOnInfo[1].getSlotStock() - 1);
                    addOnInfo[1].setSlotSold(addOnInfo[1].getSlotSold() + 1);
                }
            }

            if (addOnNames.contains("Ketchup")) {
                if(isConfirmed) {
                    addOnInfo[2].setSlotStock(addOnInfo[2].getSlotStock() - 1);
                    addOnInfo[2].setSlotSold(addOnInfo[2].getSlotSold() + 1);
                }
            }

            extraRiceRadioButton.setSelected(false);
            gravyRadioButton.setSelected(false);
            ketchupRadioButton.setSelected(false);
            isConfirmed = false;

            addOnPrices = 0;
            addOnCals = 0;
            addOnNames = "\nAdditional:\n";

            return true;

        } else if (!isSpecial && totalPayment >= vmSlots[slotNum].getSlotPrice()){
            for (int i = 0; i < 8; i++) {
                // check if only one spinner has a non-zero value
                if ((Integer) slotSpinners[i].getValue() == 1) {
                    spinCounter++;
                }

                // check if the value is just one
                if ((Integer) slotSpinners[i].getValue() > 1) {
                    notOne = true;
                }
            }

            if (spinCounter == 1 && !notOne) {
                int[] spinnerValues = {(Integer) slot1.getValue(), (Integer) slot2.getValue()
                        , (Integer) slot3.getValue(), (Integer) slot4.getValue(), (Integer) slot5.getValue()
                        , (Integer) slot6.getValue(), (Integer) slot7.getValue(), (Integer) slot8.getValue()};

                for (int i = 0; i < 8; i++) {
                    if (spinnerValues[i] == 1) {
                        nInd = i;
                        slotNum = nInd;
                    }
                }

                // subtract one from stock display
                vmSlots[nInd].setSlotStock(vmSlots[nInd].getSlotStock() - 1);

                // remove last element from arraylist of items
                vmSlots[nInd].getItems().remove(vmSlots[nInd].getItems().size() - 1);

                slotSpinners[nInd].setModel(new SpinnerNumberModel(0, 0,
                        vmSlots[nInd].getSlotStock(), 1));

                totalSales += vmSlots[nInd].getSlotPrice(); // add price to total sales
                vmSlots[nInd].setSlotSold(vmSlots[nInd].getSlotSold() + 1); // add to total quantity sold

                JOptionPane.showMessageDialog(specialVM, vmSlots[nInd].getSlotItem() + " Dispensed!");
                return true;
            }
        } else {
            JOptionPane.showMessageDialog(specialVM, "Insufficient Payment inserted!");
        }

        extraRiceRadioButton.setSelected(false);
        gravyRadioButton.setSelected(false);
        ketchupRadioButton.setSelected(false);

        addOnPrices = 0;
        addOnCals = 0;
        addOnNames = "\nAdditional:\n";

        return false;
    }

    /**
     * this method gets the cumulative amount of calories from regular items
     * to be set as the amount of calories for special items
     * @return amount of calories for special items
     */
    public int getTotalCals() {
        int totalCalories = 0;
        for (int i = 0; i < 8; i++) {
            if ((Integer) slotSpinners[i].getValue() >= 1) {
                totalCalories += (vmSlots[i].getSlotCals() * (Integer) slotSpinners[i].getValue());
            }
        }

        return totalCalories;
    }

    /**
     * this method gets the array index of a selected slot
     * @return array index of a selected slot
     */
    public int getSelectedSlot() {
        for (int i = 0; i < 8; i++) {
            if ((Integer) slotSpinners[i].getValue() == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method produces change by subtracting the inputted payment by a selected item's price
     * @return String containing breakdown of bill denominations for change
     */
    public String produceChange() {
        int change;
        if (isSpecial) {
            change = totalPayment - specialPrice;
        } else {
            change = totalPayment - vmSlots[slotNum].getSlotPrice();
        }

        int[] billDenominations = {1000, 500, 200, 100, 50, 20, 10, 5, 1};
        int[] billCounter = new int[9];
        int[] stock = money.getDenominationStock();

        updateProcessDisplay("Producing change...\n" + "Your change is: PHP " + change + ".00");
        for (int i = 0; i < 9; i++) {
            // get the count for each bill in the change
            if (change >= billDenominations[i]) {
                billCounter[i] = change / billDenominations[i];
                change = change % billDenominations[i];
                stock[i] -= billCounter[i]; // subtract change from money inventory

            }
        }
        money.setDenominationStock(stock); // update stock of bills & coins in inventory

        StringBuilder breakdown = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            if (billCounter[i] != 0) {
                breakdown.append(billDenominations[i]).append(": ").append(billCounter[i]).append(" pc/s\n");
            }
        }

        if (!isSpecial) {
            JOptionPane.showMessageDialog(specialVM, "Your change is:\n" + breakdown);
        }

        return breakdown.toString();
    }

    /**
     * this method resets the value of slot spinners
     */
    public void resetSpinners() {
        for (int i = 0; i < 8; i++) {
            slotSpinners[i].setValue(0);
        }
    }

    /**
     * this method initializes information related to add-on items
     * @return array of add-on items
     */
    public Slot[] initAddOns() {
        String[] addOnNames = {"Extra Rice", "Gravy", "Ketchup"};
        int[] addOnCals = {121, 64, 21};
        int[] addOnPrice = {15, 5, 5};

        Slot addOn1 = new Slot();
        Slot addOn2 = new Slot();
        Slot addOn3 = new Slot();

        Slot[] addOnsTemp = {addOn1, addOn2, addOn3};

        for (int i = 0; i < 3; i++) {
            addOnsTemp[i].setSlotPrice(addOnPrice[i]);
            addOnsTemp[i].setSlotCals(addOnCals[i]);
            addOnsTemp[i].setSlotItem(addOnNames[i]);

            // radiobutton accepts html (used to apply line breaks in this case)
            addOns[i].setText("<html>" + addOnNames[i] + "<br />" + addOnsTemp[i].getSlotStock() +
                    " pc/s available<br/>" + addOnCals[i] + " calories<br/>" + "+PHP "
                    + addOnsTemp[i].getSlotPrice() + ".00</html>");

            // add to arraylist of items
            for (int j = 0; j < 10; j++) {
                addOnsTemp[i].getItems().add(new Item(addOnNames[i], addOnPrice[i], addOnCals[i]));
            }
        }

        ricePic.setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("design_images/ikstrarays.png"))).getImage()
                .getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
        gravyPic.setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("design_images/gribi.png"))).getImage()
                .getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
        ketchupPic.setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("design_images/catsup.png"))).getImage()
                .getScaledInstance(80, 80, Image.SCALE_DEFAULT)));

        return addOnsTemp;
    }

    /**
     * this method sets the information to be displayed about add-on items
     */
    public void updateAddOnDisplay() {
        for (int i = 0; i < 3; i++) {
            addOns[i].setText("<html>" + addOnInfo[i].getSlotItem() + "<br />" + addOnInfo[i].getSlotStock() +
                    " pc/s available<br/>" + addOnInfo[i].getSlotCals() + " calories<br/>"
                    + "+ PHP " + addOnInfo[i].getSlotPrice() + ".00</html>");
        }
    }

    /**
     * this method checks if an add-on item radio button is selected
     * @return radio button is selected status
     */
    public boolean isChecked() {
        boolean isChecked = false;
        for (int i = 0; i < 3; i++) {
            if (addOns[i].isSelected()) {
                isChecked = true;
            }
        }

        return isChecked;
    }

    /**
     * this method allows operators to click on slot spinners
     */
    public void enableSpinners() {
        for (JSpinner slotSpinner : slotSpinners) {
            slotSpinner.setEnabled(true);
        }
    }

    /**
     * this method prevents operators from being able to click on slot spinners
     */
    public void disableSpinners() {
        for (JSpinner slotSpinner : slotSpinners) {
            slotSpinner.setEnabled(false);
        }
    }
}
