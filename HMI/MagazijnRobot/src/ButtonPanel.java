import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class ButtonPanel extends JPanel implements ActionListener {
    private JButton jbOrder;
    private OrderPanel orderPanel;

    private boolean emergencyStatus;

    private JButton emergencyStop;
    public ButtonPanel(OrderPanel orderPanel){
        setPreferredSize(new Dimension(350,200));
        setLayout(new FlowLayout());
        setBackground(Color.white);
        setBorder(new MatteBorder(1, 0, 0, 1, Color.BLACK));

        this.orderPanel = orderPanel;

        jbOrder = new JButton("Order inladen");

        jbOrder.addActionListener(this);
        emergencyStop = new JButton("Schakel noodstop in");
        emergencyStop.addActionListener(this);
        emergencyStop.setPreferredSize(new Dimension(340,30));
       // emergencyStop.setBackground(Color.red);
        emergencyStop.setFocusable(false);
        emergencyStop.setBorder(BorderFactory.createLineBorder(Color.black));
        jbOrder.setFocusable(false);
        add(emergencyStop);
        add(jbOrder);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbOrder){
            try {
                OrderDialog dialog = new OrderDialog(orderPanel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource()== emergencyStop){
            // SerialCommunicator serialCommunicator = new SerialCommunicator("COM4",500000);
            if (!emergencyStatus) {
                //try {
              //      serialCommunicator.sendMessageToArduino("STOP");
                UIManager.put("OptionPane.noButtonText", "Nee");
                UIManager.put("OptionPane.yesButtonText", "Ja");
                int choice = JOptionPane.showConfirmDialog(null, "Wil je de noodstop vergrendelen?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Noodstop vergrendeld.");
                    emergencyStatus = true;
                    emergencyStop.setText("Schakel noodstop uit");
                } else if (choice == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Noodstop niet vergrendeld.");
                }
               // jPopupMenu.setLayout(new FlowLayout());
             //   } catch (IOException ex) {
                 //   throw new RuntimeException(ex);
               // }
            }
            else{
                UIManager.put("OptionPane.yesButtonText", "Ja");
                UIManager.put("OptionPane.noButtonText", "Nee");
                int choice = JOptionPane.showConfirmDialog(null, "Wil je de noodstop ontgrendelen?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Noodstop ontgrendeld.");
                    emergencyStop.setText("Schakel noodstop in");
                    emergencyStatus = false;
                    //      serialCommunicator.sendMessageToArduino("Unlock");
                } else if (choice == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Noodstop niet ontgrendeldt.");
                }
            }


        }
    }
}
