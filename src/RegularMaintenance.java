import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * This class represents a regular vending machine's maintenance state.
 * It displays all relevant information such as the item's type, price, stock, and calories
 * as well as the stock of denominations of money stored in the vending machine.
 * It possesses features for the machine's operator such as restocking or stocking items,
 * replenishing money stash, setting an item's price, collecting payments, and displaying a transaction summary.
 */
public class RegularMaintenance extends JFrame {
    private JTextPane item1;
    private JTextPane item3;
    private JTextPane item5;
    private JTextPane item7;
    private JTextPane item2;
    private JTextPane item4;
    private JTextPane item6;
    private JTextPane item8;
    private final JTextPane[] slotDisplay = {item1, item2, item3, item4, item5, item6, item7, item8};
    private JButton stockRestockItemsButton;
    private JButton setItemPriceButton;
    private JButton collectPaymentsButton;
    private JButton replenishMoneyStashButton;
    private JButton printTransactionSummaryButton;
    private JTextPane processDisplay;
    private JButton backButton;
    private JPanel regularMain;
    private JTextPane moneyDisplay;
    private JScrollPane scrollContent;
    private final Slot[] vmSlots;
    private int[] moneyStock;
    private int totalSales;
    private int totalSalesPrint = 0;

    /**
     * gets the total sales of a vending machine and stores it for collection
     * @return the amount of money obtained from the user
     */
    public int getTotalSales() {
        return totalSales;
    }

    /**
     * this method retrieves the cumulative value of total sales, collected and uncollected,
     * to be displayed in the transaction summary
     * @return the total sales of a vending machine
     */
    public int getTotalSalesPrint() {
        return totalSalesPrint;
    }

    /**
     * this method sets the amount stored in the vending machine of a specific denomination of money
     * @param moneyStock the array containing the stock of denominations of money
     */
    public void setMoneyStock(int[] moneyStock) {
        this.moneyStock = moneyStock;
    }

    /**
     * this method sets the total sales of the vending machine for collection
     * @param totalSales the amount of money obtained from the user
     */
    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * this method sets the cumulative value of total sales, collected and uncollected,
     * to be displayed in the transaction summary
     * @param totalSalesPrint the total sales of a vending machine
     */
    public void setTotalSalesPrint(int totalSalesPrint) {
        this.totalSalesPrint = totalSalesPrint;
    }

    /**
     * this method gets the content of a text pane which is the details of an item
     * @return the text panes containing the details of slot items
     */
    public JTextPane[] getSlotDisplay() {
        return slotDisplay;
    }

    /**
     * this method gets the stock of money denominations stored in the vending machine
     * @return the stock of money denominations stored in the vending machine
     */
    public int[] getMoneyStock() {
        return moneyStock;
    }

    /**
     * this method gets the text pane which displays the stock of money denominations
     * stored in the vending machine
     * @return the text pane displaying the stock of money denominations stored in the vending machine
     */
    public JTextPane getMoneyDisplay() {
        return moneyDisplay;
    }

    /**
     * this method gets the text pane which displays the status of the processes
     * being done by the vending machine
     * @return the text pane containing process status
     */
    public JTextPane getProcessDisplay() {
        return processDisplay;
    }

    /**
     * This is the only constructor method in this class,
     * and it initializes all related variables and JComponents to be found on the regular maintenance screen
     * @param sourceFrame instance of the TestVM class
     * @param vmSlots array of slots in the vending machine
     * @param moneyStock array of the stock of money denominations
     */
    public RegularMaintenance(TestVM sourceFrame, Slot[] vmSlots, int[] moneyStock) {
        this.vmSlots = vmSlots;
        this.moneyStock = moneyStock;

        initTextPanes(slotDisplay);
        initMoneyStock(moneyStock, moneyDisplay);

        setContentPane(regularMain);
        setTitle("Regular Vending Machine - Maintenance Mode");
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

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                sourceFrame.setVisible(true);
            }
        });
        stockRestockItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stockOrRestock(askSlotNum(), vmSlots, regularMain);
                initTextPanes(slotDisplay);
            }
        });
        setItemPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setItemPrice(askSlotNum(), vmSlots, regularMain);
                initTextPanes(slotDisplay);
            }
        });
        collectPaymentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getTotalSales() > 0)
                    JOptionPane.showMessageDialog(regularMain, "PHP " + getTotalSales() + ".00 collected!");
                else
                    JOptionPane.showMessageDialog(regularMain, "No sales have been made yet!");
                setTotalSales(0);
                updateProcessDisplay("Total Sales: PHP " + getTotalSales() + ".00", processDisplay);
                sourceFrame.getRegularVM().setTotalSales(0);
            }
        });
        printTransactionSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(regularMain, printTransactionSummary(vmSlots).toString());
            }
        });
        replenishMoneyStashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replenishMoneyStash(askMoneyDenom(), moneyStock, moneyDisplay, regularMain);
            }
        });
    }

    /**
     * this method initializes the text panes for each item slot in the maintenance screen
     * @param slotDisplay the array of text panes for slot item details
     */
    public void initTextPanes(JTextPane[] slotDisplay) {
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
     * this method sets the text to be displayed in the process display text pane and formats it
     * @param text process description to be displayed
     * @param processDisplay text pane containing @param text
     */
    public void updateProcessDisplay(String text, JTextPane processDisplay) {
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
     * this method displays the stock of money denominations and formats the text within the text pane
     * @param moneyStock the amount of each denomination of money
     * @param moneyDisplay the text pane displaying the stock of money denominations
     */
    public void initMoneyStock(int[] moneyStock, JTextPane moneyDisplay) {
        // align text to center
        StyledDocument style = moneyDisplay.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
        style.setParagraphAttributes(0, style.getLength(), align, false);

        String moneyStockDisplay = "PHP 1000: " + moneyStock[0] + " | PHP 500: " +
                moneyStock[1] + " | PHP 200: " + moneyStock[2] + " | PHP 100: " + moneyStock[3] + " | PHP 50: " + moneyStock[4] +
                " | PHP 20: " + moneyStock[5] + " | PHP 10: " + moneyStock[6] + " | PHP 5: " +
                moneyStock[7] + " | PHP 1: " + moneyStock[8];

        moneyDisplay.setText(moneyStockDisplay);
    }

    /**
     * this method gets the item slot array index via slot number options in an option pane
     * @return item slot array index
     */
    public int askSlotNum() {
        try {
            String[] options = {"1", "2", "3", "4", "5", "6", "7", "8"};
            String slotNum = (String) JOptionPane.showInputDialog(null, "Select slot number:",
                    "Slot Number Selection", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (slotNum == null) {
                dispose();
                this.setVisible(true);
            } else {
                return Integer.parseInt(slotNum) - 1;
            }
        } catch (NumberFormatException ignored) {

        }
        return -1;
    }

    /**
     * this method selects the denomination of money to be restocked
     * @return money array slot index
     */
    public int askMoneyDenom() {
        String[] options = {"PHP 1000.00", "PHP 500.00", "PHP 200.00", "PHP 100.00", "PHP 50.00",
                "PHP 20.00", "PHP 10.00", "PHP 5.00", "PHP 1.00"};
        String denomInd = (String) JOptionPane.showInputDialog(null,
                "Select denomination to restock:", "Denomination Selection",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (denomInd == null) {
            dispose();
            this.setVisible(true);
        } else {
            return Arrays.asList(options).indexOf(denomInd);
        }

        return -1;
    }

    /**
     * this method adds to the stock of an item in a slot
     * @param slotNum number referring to slot array index
     * @param vmSlots array of slots in the vending machine
     * @param regularMain panel containing the contents of the maintenance screen
     */
    public void stockOrRestock(int slotNum, Slot[] vmSlots, JPanel regularMain) {
        try {
            String initVal;
            int stockNum = 0;

            if (slotNum != -1) {
                initVal = JOptionPane.showInputDialog("Input stock to add:");
                if (initVal != null) {
                    stockNum = Integer.parseInt(initVal);
                    if (stockNum > 0) {
                        vmSlots[slotNum].setSlotStock(vmSlots[slotNum].getSlotStock() + stockNum);
                    } else {
                        JOptionPane.showMessageDialog(regularMain, "Cannot add " + stockNum + " to stock!");
                    }
                } else {
                    dispose();
                    this.setVisible(true);
                }

                if (stockNum > 0) {
                    for (int i = 0; i < stockNum; i++) {
                        vmSlots[slotNum].getItems().add(new Item(vmSlots[slotNum].getSlotItem(), vmSlots[slotNum].getSlotPrice(),
                                vmSlots[slotNum].getSlotCals()));
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(regularMain, "Invalid input!");
        }

    }

    /**
     * this method adds to the stock of money denominations in the vending machine
     * @param denomInd index of denomination in money stock array
     * @param moneyStock array containing stock of money denominations
     * @param moneyDisplay text pane that displays the stock of money denominations
     * @param regularMain panel containing the contents of the maintenance screen
     */
    public void replenishMoneyStash(int denomInd, int[] moneyStock, JTextPane moneyDisplay, JPanel regularMain) {
        try {
            String initVal;
            int stockNum;

            if (denomInd != -1) {
                initVal = JOptionPane.showInputDialog("Input amount (pcs) to add:");
                if (initVal != null) {
                    stockNum = Integer.parseInt(initVal);
                    if (stockNum > 0) {
                        moneyStock[denomInd] += stockNum;
                        initMoneyStock(moneyStock, moneyDisplay);
                    } else {
                        JOptionPane.showMessageDialog(regularMain, "Cannot add " + stockNum + " to stock!");
                    }
                } else {
                    dispose();
                    this.setVisible(true);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(regularMain, "Invalid input!");
        }
    }

    /**
     * this method sets the price of an item in a specific slot in the vending machine
     * @param slotNum number referring to slot array index
     * @param vmSlots array of slots in the vending machine
     * @param regularMain panel containing the contents of the maintenance screen
     */
    public void setItemPrice(int slotNum, Slot[] vmSlots, JPanel regularMain) {
        try {
            String initVal;
            int itemPrice;

            if (slotNum != -1) {
                initVal = JOptionPane.showInputDialog("Input price for " +
                        vmSlots[slotNum].getSlotItem() + ":");

                if (initVal != null) {
                    itemPrice = Integer.parseInt(initVal);
                    if (itemPrice > 0) {
                        vmSlots[slotNum].setSlotPrice(itemPrice);
                    } else {
                        JOptionPane.showMessageDialog(regularMain, "Cannot set price to PHP "
                                + itemPrice + ".00!");
                    }
                } else {
                    dispose();
                    this.setVisible(true);
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(regularMain, "Invalid input!");
        }
    }

    /**
     * this method displays the initial and current stock of the vending machine as well as the total sales
     * @param vmSlots array of slots in the vending machine
     * @return a StringBuilder object, to be converted into string and displayed
     * when the user presses the print transaction summary button
     */
    public StringBuilder printTransactionSummary(Slot[] vmSlots) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("--   Transaction Summary   --\nInitial Inventory:\n");

        // print initial stock
        for (int i = 0; i < vmSlots.length; i++) {
            receipt.append(vmSlots[i].getSlotItem() + " - " + (vmSlots[i].getSlotStock() +
                    vmSlots[i].getSlotSold()) + "\n");
        }
        receipt.append("------------------------\nCurrent Inventory:\n");

        // print current stock
        for (int i = 0; i < vmSlots.length; i++) {
            receipt.append(vmSlots[i].getSlotItem() + " - " + vmSlots[i].getSlotStock() + "\n");
        }

        receipt.append("------------------------\nTotal Sales: PHP " + totalSalesPrint + ".00");

        return receipt;
    }
}
