import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents a special vending machine's maintenance state.
 * It displays all relevant information such as the add-on or item's type, price, stock, and calories
 * as well as the stock of denominations of money stored in the vending machine.
 * It possesses features for the machine's operator such as restocking or stocking items and add-ons,
 * replenishing money stash, setting an add-on or item's price, collecting payments, and displaying a transaction summary.
 */
public class SpecialMaintenance extends RegularMaintenance {
    private JPanel specialMain;
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
    private JTextPane moneyDisplay;
    private JTextPane addOn1TextPane;
    private JTextPane addOn2TextPane;
    private JTextPane addOn3TextPane;
    private JLabel ricePic;
    private JLabel gravyPic;
    private JLabel ketchupPic;
    private JScrollPane mainScroll;
    private JTextPane[] addOnDisplay = {addOn1TextPane, addOn2TextPane, addOn3TextPane};
    private Slot[] addOns;
    private Slot[] vmSlots;
    private int[] moneyStock;
    private int totalSales;
    private int totalSalesPrint = 0;

    /**
     * this method gets the stock of money denominations stored in the vending machine
     * @return the stock of money denominations stored in the vending machine
     */
    @Override
    public int[] getMoneyStock() {
        return moneyStock;
    }

    /**
     * this method gets the text pane which displays the stock of money denominations
     * stored in the vending machine
     * @return the text pane displaying the stock of money denominations stored in the vending machine
     */
    @Override
    public JTextPane getMoneyDisplay() {
        return moneyDisplay;
    }

    /**
     * this method gets the content of a text pane which is the details of an item
     * @return the text panes containing the details of slot items
     */
    @Override
    public JTextPane[] getSlotDisplay() {
        return slotDisplay;
    }

    /**
     * this method gets the text pane which displays the status of the processes
     * being done by the vending machine
     * @return the text pane containing process status
     */
    @Override
    public JTextPane getProcessDisplay() {
        return processDisplay;
    }

    /**
     * this method sets the total sales of the vending machine for collection
     * @param totalSales the amount of money obtained from the user
     */
    @Override
    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * this method sets the cumulative value of total sales, collected and uncollected,
     * to be displayed in the transaction summary
     * @param totalSalesPrint the total sales of a vending machine
     */
    @Override
    public void setTotalSalesPrint(int totalSalesPrint) {
        this.totalSalesPrint = totalSalesPrint;
    }

    /**
     * this method retrieves the cumulative value of total sales, collected and uncollected,
     * to be displayed in the transaction summary
     * @return the total sales of a vending machine
     */
    @Override
    public int getTotalSalesPrint() {
        return totalSalesPrint;
    }

    /**
     * This is the only constructor method in this class,
     * and it initializes all related variables and JComponents to be found on the special maintenance screen
     * @param sourceFrame instance of the TestVM class
     * @param vmSlots array of slots in the vending machine
     * @param addOns array of add-on slot details
     * @param moneyStock array of the stock of money denominations
     */
    public SpecialMaintenance(TestVM sourceFrame, Slot[] vmSlots, Slot[] addOns, int[] moneyStock) {
        super(sourceFrame, vmSlots, moneyStock);

        this.vmSlots = vmSlots;
        this.moneyStock = moneyStock;
        this.addOns = addOns;

        initTextPanes(slotDisplay);
        initMoneyStock(moneyStock, moneyDisplay);
        initAddOnDisplay();

        setContentPane(specialMain);
        setTitle("Special Vending Machine - Maintenance Mode");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 816);
        setLocationRelativeTo(null);
        setResizable(false);

        // faster scrolling
        mainScroll.getVerticalScrollBar().setUnitIncrement(13);

        // change scrollbar color
        mainScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.decode("#DC0000");
            }
        });
        mainScroll.getVerticalScrollBar().getComponent(0).setBackground(Color.decode("#FEAF17"));
        mainScroll.getVerticalScrollBar().getComponent(1).setBackground(Color.decode("#FEAF17"));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                sourceFrame.setVisible(true);
            }
        });
        printTransactionSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(specialMain, printTransactionSummary(vmSlots, addOns).toString());
            }
        });
        collectPaymentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (totalSales > 0)
                    JOptionPane.showMessageDialog(specialMain, "PHP " + totalSales + ".00 collected!");
                else
                    JOptionPane.showMessageDialog(specialMain, "No sales have been made yet!");
                totalSales = 0;
                updateProcessDisplay("Total Sales: PHP " + totalSales + ".00", processDisplay);
                sourceFrame.getSpecialVM().setTotalSales(0);
            }
        });
        replenishMoneyStashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replenishMoneyStash(askMoneyDenom(), moneyStock, moneyDisplay, specialMain);
            }
        });
        setItemPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int itemOrAddOn = askItemOrAddOn();

                if (itemOrAddOn == 0) {
                    setItemPrice(askSlotNum(), vmSlots, specialMain);
                    initTextPanes(slotDisplay);
                } else if (itemOrAddOn == 1) {
                    setItemPrice(askAddOnInd(), addOns, specialMain);
                    initAddOnDisplay();
                }
            }
        });
        stockRestockItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int itemOrAddOn = askItemOrAddOn();

                if (itemOrAddOn == 0) {
                    stockOrRestock(askSlotNum(), vmSlots, specialMain);
                    initTextPanes(slotDisplay);
                } else if (itemOrAddOn == 1) {
                    stockOrRestock(askAddOnInd(), addOns, specialMain);
                    initAddOnDisplay();
                }

            }
        });
    }

    /**
     * this method initializes the JComponents related to add-on detail display
     */
    public void initAddOnDisplay() {
        for (int i = 0; i < 3; i++) {
            // align text to center
            StyledDocument style = addOnDisplay[i].getStyledDocument();
            SimpleAttributeSet align = new SimpleAttributeSet();
            StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
            style.setParagraphAttributes(0, style.getLength(), align, false);

            // assign contents of text pane
            addOnDisplay[i].setText(addOns[i].getSlotItem() + "\nPHP " + addOns[i].getSlotPrice() + ".00\n" + "(" +
                    addOns[i].getSlotStock() + " pcs available)\n" + addOns[i].getSlotCals() + " calories");
            addOnDisplay[i].setEditable(false);
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
    }

    /**
     * this method asks operators which add-on they will restock
     * @return options array element index
     */
    public int askAddOnInd() {
        String[] options = {"Extra Rice", "Gravy", "Ketchup"};
        String optInd = (String) JOptionPane.showInputDialog(null,
                "Select add-on to restock:", "Add-on Selection",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        return Arrays.asList(options).indexOf(optInd);
    }

    /**
     * this method asks operators if an item to be selected is a regular item or an add-on
     * @return options array element index
     */
    public int askItemOrAddOn() {
        String[] options = {"Regular Item", "Add-on"};
        String optInd = (String) JOptionPane.showInputDialog(null,
                "Select type of item:", "Item Type Selection",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        return Arrays.asList(options).indexOf(optInd);
    }

    /**
     * this method displays the initial and current stock of the vending machine as well as the total sales
     * @param vmSlots array of slots in the vending machine
     * @param addOns array of add-on slot details
     * @return StringBuilder, to be converted into string and displayed
     * when the user presses the print transaction summary button
     */
    public StringBuilder printTransactionSummary(Slot[] vmSlots, Slot[] addOns) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("--   Transaction Summary   --\nInitial Inventory:\n");

        // print initial stock
        for (int i = 0; i < vmSlots.length; i++) {
            receipt.append(vmSlots[i].getSlotItem() + " - " + (vmSlots[i].getSlotStock() +
                    vmSlots[i].getSlotSold()) + "\n");
        }

        // print add-ons initial stock
        for (int i = 0; i < addOns.length; i++) {
            receipt.append(addOns[i].getSlotItem() + " - " + (addOns[i].getSlotStock()
                    + addOns[i].getSlotSold()) + "\n");
        }

        receipt.append("------------------------\nCurrent Inventory:\n");

        // print current stock
        for (int i = 0; i < vmSlots.length; i++) {
            receipt.append(vmSlots[i].getSlotItem() + " - " + vmSlots[i].getSlotStock() + "\n");
        }

        // print add-ons current stock
        for (int i = 0; i < addOns.length; i++) {
            receipt.append(addOns[i].getSlotItem() + " - " + addOns[i].getSlotStock() + "\n");
        }

        receipt.append("------------------------\nTotal Sales: PHP " + totalSalesPrint + ".00");

        return receipt;
    }
}
