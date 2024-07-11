import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents the main menu of a vending machine factory simulator.
 * It allows operators to make a choice between creating and testing vending machines.
 */
public class MainMenu extends JFrame {
    private JPanel mainMenu;
    private JLabel menuTitle;
    private JButton createButton;
    private JButton testButton;
    private JButton quitButton;
    private JPanel buttonsPanel;
    private CreateVM createVM;
    private TestVM testVM;
    private RegularVM currentRegular;
    private RegularMaintenance currentRegularMain;
    private SpecialVM currentSpecial;
    private SpecialMaintenance currentSpecialMain;
    private int vmType = 0;

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
     * and it instantiates all related objects and JComponents to be found on the main menu screen
     * upon the creation of a Main Menu instance.
     */
    public MainMenu() {
        createVM = new CreateVM(this);
        testVM = new TestVM(this);

        setContentPane(mainMenu);

        createButton.setMargin(new Insets(10, 0, 10, 0));
        testButton.setMargin(new Insets(10, 0, 10, 0));
        quitButton.setMargin(new Insets(10, 0, 10, 0));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Vending Machine Factory Simulator");
        setSize(860, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        // jOptionPane customization
        UIManager.put("OptionPane.background", Color.decode("#FFFAF4"));
        UIManager.getLookAndFeelDefaults().put("Panel.background", Color.decode("#FFFAF4"));
        UIManager.put("OptionPane.messageFont", new Font("VAG Rounded", Font.BOLD, 14));
        UIManager.put("OptionPane.buttonFont", new Font("VAG Rounded", Font.PLAIN, 12));

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                createVM.setVisible(true);
                if (vmType == 1) {
                    currentRegular = new RegularVM(testVM); // make new regularVm jFrame
                    currentRegularMain = new RegularMaintenance(testVM, currentRegular.getVmSlots(),
                            currentRegular.getMoney().getDenominationStock()); // make new maintenance jFrame
                    testVM.setRegularVM(currentRegular);
                    testVM.setRegularMain(currentRegularMain);
                } else if (vmType == 2) {
                    currentSpecial = new SpecialVM(testVM);
                    currentSpecialMain = new SpecialMaintenance(testVM, currentSpecial.getVmSlots(),
                            currentSpecial.getAddOnInfo(), currentSpecial.getMoney().getDenominationStock());
                    testVM.setSpecialVM(currentSpecial);
                    testVM.setSpecialMain(currentSpecialMain);
                }
            }
        });
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                testVM.setVmType(vmType);
                testVM.setVisible(true);
            }
        });
    }

    /**
     * Our main method. This is where the instance of the MainMenu class is declared.
     * This is where the program is given full functionality. All other object instantiations are
     * allowed starting from here.
     * @param args The command line arguments.
     **/
    public static void main(String[] args) {
        new MainMenu();
    }
}
