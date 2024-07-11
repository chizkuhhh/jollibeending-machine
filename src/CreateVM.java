import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The CreateVM class represents the menu where vending machine creation takes place.
 * It allows the operator to choose between creating a regular or special vending machine.
 */
public class CreateVM extends JFrame {
    private JPanel createVM;
    private JLabel createTitle;
    private JButton regularVendingMachineButton;
    private JButton specialVendingMachineButton;
    private JButton backButton;

    /**
     * This constructor method initializes all related variables
     * and JComponents to be found on the create vending machine screen
     * @param sourceFrame instance of the MainMenu class
     */
    public CreateVM(MainMenu sourceFrame) {
        setContentPane(createVM);
        setTitle("Create Vending Machine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                sourceFrame.setVisible(true);
            }
        });
        regularVendingMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceFrame.setVmType(1);
                JOptionPane.showMessageDialog(createVM, "Regular Vending Machine Created!");
                dispose();
                sourceFrame.setVisible(true);

            }
        });
        specialVendingMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceFrame.setVmType(2);
                JOptionPane.showMessageDialog(createVM, "Special Vending Machine Created!");
                dispose();
                sourceFrame.setVisible(true);
            }
        });
    }
}
