import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents the test vending machine menu from which an operator is able to
 * choose between accessing vending features and maintenance features.
 */
public class TestVM extends JFrame{
    private JPanel testVM;
    private JButton vendingFeaturesButton;
    private JButton maintenanceFeaturesButton;
    private JButton backButton;
    private RegularVM regularVM = new RegularVM(this);
    private RegularMaintenance regularMain = new RegularMaintenance(this, regularVM.getVmSlots(),
            regularVM.getMoney().getDenominationStock());
    private SpecialVM specialVM = new SpecialVM(this);
    private SpecialMaintenance specialMain = new SpecialMaintenance(this, specialVM.getVmSlots(),
            specialVM.getAddOnInfo(), specialVM.getMoney().getDenominationStock());
    private int vmType = 0;

    /**
     * This method gets the instance of a regular vending machine
     * @return instance of a regular vending machine
     */
    public RegularVM getRegularVM() {
        return regularVM;
    }

    /**
     * This method gets the instance of a special vending machine
     * @return instance of a special vending machine
     */
    public SpecialVM getSpecialVM() {
        return specialVM;
    }

    /**
     * this method sets an instance of a regular vending machine's vending screen
     * @param regularVM instance of a regular vending machine's vending screen
     */
    public void setRegularVM(RegularVM regularVM) {
        this.regularVM = regularVM;
    }

    /**
     * this method sets an instance of a regular vending machine's maintenance screen
     * @param regularMain instance of a regular vending machine's maintenance screen
     */
    public void setRegularMain(RegularMaintenance regularMain) {
        this.regularMain = regularMain;
    }

    /**
     * this method sets an instance of a special vending machine's vending screen
     * @param specialVM instance of a special vending machine's vending screen
     */
    public void setSpecialVM(SpecialVM specialVM) {
        this.specialVM = specialVM;
    }

    /**
     * this method sets an instance of a special vending machine's maintenance screen
     * @param specialMain instance of a special vending machine's maintenance screen
     */
    public void setSpecialMain(SpecialMaintenance specialMain) {
        this.specialMain = specialMain;
    }

    /**
     * This method sets the number corresponding to the type of vending machine
     * to either 1 or 2
     * @param vmType the number corresponding to the type of vending machine
     */
    public void setVmType(int vmType) {
        this.vmType = vmType;
    }

    /**
     * This is the only constructor method in this class,
     * and it initializes all related variables and JComponents to be found on the test vending machine screen
     * @param sourceFrame instance of the MainMenu class
     */
    public TestVM(MainMenu sourceFrame) {
        setContentPane(testVM);
        setTitle("Test Vending Machine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        vendingFeaturesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (vmType == 1) {
                    regularVM.initTextPanes();
                    regularVM.setVisible(true);
                } else if (vmType == 2) {
                    specialVM.initTextPanes();
                    specialVM.updateAddOnDisplay();
                    specialVM.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(testVM, "Error! No Vending Machine Created!");
                    sourceFrame.setVisible(true);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                sourceFrame.setVisible(true);
            }
        });
        maintenanceFeaturesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (vmType == 1) {
                    regularMain.setMoneyStock(regularVM.getMoney().getDenominationStock());
                    regularMain.initTextPanes(regularMain.getSlotDisplay());
                    regularMain.initMoneyStock(regularMain.getMoneyStock(), regularMain.getMoneyDisplay());
                    regularMain.setTotalSales(regularVM.getTotalSales());
                    regularMain.setTotalSalesPrint(regularMain.getTotalSalesPrint() + regularVM.getTotalSales());
                    regularMain.updateProcessDisplay("Select an option to begin...\n" + "Total Sales: PHP " +
                            regularMain.getTotalSales() + ".00", regularMain.getProcessDisplay());
                    dispose();
                    regularMain.setVisible(true);
                } else if (vmType == 2) {
                    specialMain.setMoneyStock(specialVM.getMoney().getDenominationStock());
                    specialMain.initTextPanes(specialMain.getSlotDisplay());
                    specialMain.initMoneyStock(specialMain.getMoneyStock(), specialMain.getMoneyDisplay());
                    specialMain.initAddOnDisplay();
                    specialMain.setTotalSales(specialVM.getTotalSales());
                    specialMain.setTotalSalesPrint(specialMain.getTotalSalesPrint() + specialVM.getTotalSales());
                    specialMain.updateProcessDisplay("Select an option to begin...\n" + "Total Sales: PHP " +
                            specialVM.getTotalSales() + ".00", specialMain.getProcessDisplay());
                    dispose();
                    specialMain.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(testVM, "Error! No Vending Machine Created!");
                    sourceFrame.setVisible(true);
                }
            }
        });
    }
}
