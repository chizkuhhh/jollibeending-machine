import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents the loading screen panels to be displayed during special item creation.
 */
public class LoadingScreen extends JFrame {
    private JPanel splashScreen;
    private JLabel statusDisplay;
    private JProgressBar prepProgress;
    private JLabel loadingIcon;
    private Timer timer;
    private int progress;
    private SpecialItemTasks tasks = new SpecialItemTasks();
    private String[] taskSet;

    /**
     * This is the only constructor method in this class,
     * and it initializes all related variables and JComponents to be found on the loading screen
     * @param specialName name of special item
     * @param breakdown change represented in denominations
     * @param specialImg image icon file name
     */
    public LoadingScreen(String specialName, String breakdown, String specialImg) {
        progress = 0;
        for (int i = 0; i < tasks.getSpecialNames().length; i++) {
            if(specialName.contains(tasks.getSpecialNames()[i])) {
                taskSet = tasks.getSpecialTasks()[i];
            }
        }

        setContentPane(splashScreen);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);

        ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource("food_images/orderpreparing.gif")).getImage().
                getScaledInstance(133, 100, Image.SCALE_DEFAULT));
        loadingIcon.setIcon(icon);
        loadingIcon.setSize(133, 100);
        Dimension size = loadingIcon.getPreferredSize();
        loadingIcon.setBounds(50, 30, size.width, size.height);

        setVisible(true);
        prepareItem(specialName, breakdown, taskSet, specialImg);
    }

    /**
     * this method sets the text of the status display
     * @param text special item preparation description
     */
    public void updateStatus(String text) {
        statusDisplay.setText(text);
    }

    /**
     * this method displays the text description of special item preparation processes and completion messages
     * @param specialName name of special item
     * @param breakdown change represented in denominations
     * @param taskSet array of Strings containing text descriptions of special item preparation processes
     * @param specialImg image icon file name
     */
    public void prepareItem(String specialName, String breakdown, String[] taskSet, String specialImg) {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 10;
                prepProgress.setValue(progress);

                if (progress < 20) {
                    updateStatus(taskSet[0]);
                } else if (progress > 20 && progress < 40) {
                    updateStatus(taskSet[1]);
                } else if (progress > 40 && progress < 60) {
                    updateStatus(taskSet[2]);
                } else if (progress > 60 && progress < 80) {
                    updateStatus(taskSet[3]);
                } else if (progress > 80 && progress < 100) {
                    updateStatus(taskSet[4]);
                } else if (progress == 100) {
                    updateStatus("JuiCyLiCiOus!");
                } else if (progress > 100) {
                    timer.stop();
                    dispose();
                    ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource(specialImg)).getImage()
                            .getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                    JOptionPane.showMessageDialog(splashScreen, specialName + "Dispensed!",
                            "Special Item", JOptionPane.INFORMATION_MESSAGE, icon);
                    JOptionPane.showMessageDialog(splashScreen, "Your change is:\n" + breakdown);
                }
            }
        });
        timer.start();
    }
}
