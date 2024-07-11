import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents a regular vending machine, which is capable of storing at least 10 items in its
 * 8 slots. It allows user to select a denomination of money to insert into the vending machine,
 * produces change in denominations for the user, allows them to select an item of their choice to purchase,
 * and displays all relevant information such as the item's type, price, stock, and calories.
 */
public class RegularVM extends JFrame {
    private JPanel regularVM;
    private JButton slot1Button;
    private JButton slot3Button;
    private JButton slot5Button;
    private JButton slot7Button;
    private JButton slot2Button;
    private JButton slot4Button;
    private JButton slot6Button;
    private JButton slot8Button;

    private JButton[] slotButtons = {slot1Button, slot2Button, slot3Button, slot4Button, slot5Button,
            slot6Button, slot7Button, slot8Button};
    private JComboBox<String> insertPayment;
    private JButton cancelButton;
    private JButton dispenseButton;
    private JButton backButton;
    private JTextPane item1;
    private JTextPane item3;
    private JTextPane item5;
    private JTextPane item7;
    private JTextPane item2;
    private JTextPane item4;
    private JTextPane item6;
    private JTextPane item8;
    private JScrollPane vmContent;
    private JTextPane processDisplay;
    private JButton insertButton;
    private JPanel slot1Panel;
    private JPanel slot2Panel;
    private JPanel slot3Panel;
    private JPanel slot4Panel;
    private JPanel slot5Panel;
    private JPanel slot6Panel;
    private JPanel slot7Panel;
    private JPanel slot8Panel;
    private JLabel item1Pic;
    private JLabel item2Pic;
    private JLabel item3Pic;
    private JLabel item4Pic;
    private JLabel item5Pic;
    private JLabel item6Pic;
    private JLabel item7Pic;
    private JLabel item8Pic;
    private final JTextPane[] slotDisplay = {item1, item2, item3, item4, item5, item6, item7, item8};

    private Slot[] vmSlots;
    private int totalPayment;
    private int slotNum;
    private int totalSales;
    private Money money = new Money();

    /**
     * This method gets the array of slots in the vending machine.
     * @return array of slots in the vending machine
     */
    public Slot[] getVmSlots() {
        return vmSlots;
    }

    /**
     * This method gets the total amount of money a user spent on items from the vending machine.
     * @return money earned by the vending machine
     */
    public int getTotalSales() {
        return totalSales;
    }

    /**
     * This method gets the total amount of money stored in a vending machine.
     * @return money stored in the vending machine
     */
    public Money getMoney() {
        return money;
    }

    /**
     * This method sets the total amount of money a user spent on items from the vending machine.
     * @param totalSales money earned by the vending machine
     */
    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * This is the only constructor method in this class, and it creates the instance of a regular vending machine.
     * @param sourceFrame is an instance of the TestVM class
     */
    public RegularVM(TestVM sourceFrame) {
        vmMethods meth = new vmMethods();

        vmSlots = meth.initSlots(); // assign values to the attributes of each slot item
        initTextPanes();; // display slot details in each textPane
        initButtons();
        disableButtons();
        dispenseButton.setEnabled(false);
        totalPayment = 0;
        totalSales = 0;

        updateProcessDisplay("Insert Money to Begin...");

        setContentPane(regularVM);
        setTitle("Regular Vending Machine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 816);
        setLocationRelativeTo(null);
        setResizable(false);

        // faster scrolling
        vmContent.getVerticalScrollBar().setUnitIncrement(13);

        // change scrollbar colors
        vmContent.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.decode("#DC0000");
            }
        });
        vmContent.getVerticalScrollBar().getComponent(0).setBackground(Color.decode("#FEAF17"));
        vmContent.getVerticalScrollBar().getComponent(1).setBackground(Color.decode("#FEAF17"));

        // add the options inside the combo box for inserting payment
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
                enableButtons();
            }
        });
        dispenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (meth.checkEnoughChange(totalPayment, vmSlots, slotNum, money)) {
                    if (dispenseItem()) {
                        produceChange();
                        totalPayment = 0;
                        updateProcessDisplay("Insert Money to Begin...");
                    } else {
                        updateProcessDisplay("Returning money...");
                        StringBuilder breakdown = new StringBuilder();
                        for (int i = 0; i < 9; i++) {
                            if (money.getPaymentBreakdown()[i] != 0) {
                                breakdown.append(money.getDenominations()[i]).append(": ").
                                        append(money.getPaymentBreakdown()[i]).append(" pc/s\n");
                            }
                        }

                        JOptionPane.showMessageDialog(regularVM, "Your change is:\n" + breakdown);
                    }
                } else {
                    JOptionPane.showMessageDialog(regularVM, "Machine low on change!");
                    updateProcessDisplay("Returning money...");
                    StringBuilder breakdown = new StringBuilder();
                    for (int i = 0; i < 9; i++) {
                        if (money.getPaymentBreakdown()[i] != 0) {
                            breakdown.append(money.getDenominations()[i]).append(": ").
                                    append(money.getPaymentBreakdown()[i]).append(" pc/s\n");
                        }
                    }

                    JOptionPane.showMessageDialog(regularVM, "Your change is:\n" + breakdown);
                }

                money.zeroPaymentBreakdown();
                disableButtons();
                dispenseButton.setEnabled(false);
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

                JOptionPane.showMessageDialog(regularVM, "Your change is:\n" + breakdown);
                totalPayment = 0;
                money.zeroPaymentBreakdown();
                disableButtons();
                dispenseButton.setEnabled(false);
            }
        });
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
     * This method initializes the buttons found the GUI frame corresponding to
     * the test vending feature function of the program
     */
    public void initButtons() {
        for (int i = 0; i < slotButtons.length; i++) {
            slotButtons[i].setMargin(new Insets(10, 0, 10, 0));
            int j = i;

            slotButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    slotNum = j+1;
                    updateProcessDisplay("Total Amount Inserted: PHP " + totalPayment +
                            ".00\n Selected Slot: " + slotNum);

                    dispenseButton.setEnabled(true);
                }
            });
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
     * This method displays a specific message depending on the success or failure of
     * item dispensing and updates stock if dispensing is successful
     * @return true or false for item dispensing status
     */
    public boolean dispenseItem() {
        if (vmSlots[slotNum - 1].getSlotStock() > 0 && totalPayment >= vmSlots[slotNum - 1].getSlotPrice()) {
            vmSlots[slotNum - 1].setSlotStock(vmSlots[slotNum - 1].getSlotStock() - 1); // subtract one from stock
            vmSlots[slotNum - 1].getItems().remove(vmSlots[slotNum - 1].getSlotStock() - 1); // remove last element from arraylist of items
            vmSlots[slotNum - 1].setSlotSold(vmSlots[slotNum - 1].getSlotSold() + 1); // add one to sold
            JOptionPane.showMessageDialog(regularVM, vmSlots[slotNum-1].getSlotItem() + " Dispensed!");
            initTextPanes();
            totalSales += vmSlots[slotNum - 1].getSlotPrice(); // add purchased item price to total sales
            return true;
        } else if (vmSlots[slotNum - 1].getSlotStock() == 0){
            JOptionPane.showMessageDialog(regularVM, vmSlots[slotNum-1].getSlotItem() + " Out of Stock!");
            return false;
        } else if (totalPayment < vmSlots[slotNum - 1].getSlotPrice()) {
            JOptionPane.showMessageDialog(regularVM, "Insufficient Payment Inserted!");
            return false;
        }
        return false;
    }

    /**
     * This method produces change by subtracting the inputted payment by a selected item's price
     */
    public void produceChange() {
        int change = totalPayment - vmSlots[slotNum - 1].getSlotPrice();
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

        // appends count for each bill if there are any
        StringBuilder breakdown = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            if (billCounter[i] != 0) {
                breakdown.append(billDenominations[i]).append(": ").append(billCounter[i]).append(" pc/s\n");
            }
        }
        JOptionPane.showMessageDialog(regularVM, "Your change is:\n" + breakdown);
    }

    /**
     * This method sets the enabled status of all buttons in the frame to true
     * which allows them to be pressed
     */
    public void enableButtons() {
        for (JButton slotButton : slotButtons) {
            slotButton.setEnabled(true);
        }
    }

    /**
     * This method sets the enabled status of all buttons in the frame to false
     * which prevents them from being pressed
     */
    public void disableButtons() {
        for (JButton slotButton : slotButtons) {
            slotButton.setEnabled(false);
        }
    }
}
