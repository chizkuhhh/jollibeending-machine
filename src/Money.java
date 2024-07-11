/**
 * This class represents the denominations of money in a vending machine.
 * It tracks the amount of each denomination, such as coins and bills, available in the machine.
 * The class provides methods to retrieve and modify the amount of each denomination.
 */
public class Money {
    private final String[] denominations = {"PHP 1000.00", "PHP 500.00", "PHP 200.00", "PHP 100.00", "PHP 50.00",
            "PHP 20.00", "PHP 10.00", "PHP 5.00", "PHP 1.00"};

    private int[] denominationStock;
    private int[] paymentBreakdown;

    /**
     * this method gets the String array containing the denominations of money
     * @return String array containing the denominations of money
     */
    public String[] getDenominations() {
        return denominations;
    }

    /**
     * this method gets the number array containing the stock of money denominations
     * @return number array containing the stock of money denominations
     */
    public int[] getDenominationStock() {
        return denominationStock;
    }

    /**
     * this method gets the array containing denominations of money generated from user payment
     * @return array containing denominations of money generated from user payment
     */
    public int[] getPaymentBreakdown() {
        return paymentBreakdown;
    }

    /**
     * this method sets the stock of money denominations
     * @param denominationStock stock of money denominations
     */
    public void setDenominationStock(int[] denominationStock) {
        this.denominationStock = denominationStock;
    }

    /**
     * this method adds to the count of money denominations generated from user payment
     * @param nInd index in the payment breakdown array
     */
    public void addDenomCount (int nInd) {
        this.paymentBreakdown[nInd]++;
    }

    /**
     * this method initializes the payment breakdown array to contain zeroes in all indices
     */
    public void zeroPaymentBreakdown() {
        paymentBreakdown = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    /**
     * This constructor method initializes the money stock of the vending machine
     * and calls zeroPaymentBreakdown() to initialize the stored payment denominations to be zero
     */
    public Money() {
        denominationStock = new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10};
        zeroPaymentBreakdown();
    }
}
