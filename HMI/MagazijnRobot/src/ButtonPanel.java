import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class ButtonPanel extends JPanel implements ActionListener {
    private JButton jbOrder;
    private static JButton jbPackingSlip;
    private OrderPanel orderPanel;

    private boolean emergencyStatus;
    private SerialCommunicator serialCommunicator = HMIScreen.serialCommunicator;
    private JButton emergencyStop;
    private static JButton jbOrderUpdate;
    public ButtonPanel(OrderPanel orderPanel){
        setPreferredSize(new Dimension(400,150));
        setLayout(new FlowLayout());
        setBackground(Color.white);
        setBorder(new MatteBorder(1, 0, 0, 1, Color.BLACK));

        //serialCommunicator = HMIScreen.serialCommunicator;
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

        jbOrderUpdate = new JButton("Order Bewerken");
        jbOrderUpdate.addActionListener(this);
        jbOrderUpdate.setFocusable(false);
        SetUpdateButtonEnabled(false);
        jbPackingSlip = new JButton("Pakbon Genereren");
        jbPackingSlip.addActionListener(this);
        jbPackingSlip.setFocusable(false);
        SetPackingButtonEnabled(false);
        add(emergencyStop);
        add(jbOrder);
        add(jbOrderUpdate);
        add(jbPackingSlip);
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
        if(e.getSource() == jbOrderUpdate){
            System.out.println("UPDATE!!!!!");
            try {
                OrderChangeDialog dialog = new OrderChangeDialog(orderPanel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == jbPackingSlip){
            try {
                PackingSlipDialog dialog = new PackingSlipDialog(orderPanel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource()== emergencyStop){

            if (!emergencyStatus) {
                UIManager.put("OptionPane.noButtonText", "Nee");
                UIManager.put("OptionPane.yesButtonText", "Ja");
                int choice = JOptionPane.showConfirmDialog(null, "Wil je de noodstop vergrendelen?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Noodstop vergrendeld.");
                    emergencyStatus = true;
                    emergencyStop.setText("Schakel noodstop uit");
                    try {
                        serialCommunicator.sendMessageToArduino("STOP");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Noodstop niet vergrendeld.");
                }

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
                    try {
                        serialCommunicator.sendMessageToArduino("Unlock");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Noodstop niet ontgrendeldt.");
                }
            }
        }
    }
    public static void SetUpdateButtonEnabled(boolean state){
        jbOrderUpdate.setEnabled(state);
    }
    public static void SetPackingButtonEnabled(boolean state){
        jbPackingSlip.setEnabled(state);
    }
}
