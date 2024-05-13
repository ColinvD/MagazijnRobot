import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class HMIScreen extends JFrame implements ActionListener,Listener {
    private static String location;
    private String status;

    public HMIScreen() throws SQLException {
        setSize(750, 650);
        getContentPane().setBackground(Color.black);
        setTitle("HMI magazijn robot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SchapPanel schap = new SchapPanel();
        StatusPanel status = new StatusPanel();
        OrderPanel order = new OrderPanel();
        ButtonPanel button = new ButtonPanel(order);


        GridBagLayout layout = new GridBagLayout(); //create grid bag layout
        this.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        add(schap, c);
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;

        add(button, c);
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        add(status, c);
        c.gridheight = 2;
        c.gridx = 1;
        c.gridy = 1;
        add(order, c);
        setVisible(true);
    }


    public static String getLocationRobot() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void onMessageReceived(String message) {
        if (!message.isEmpty()){
            Grid.locationRobot2 = (message.substring(0,message.indexOf(',')));
            status =  message.substring(message.indexOf(','));
        }
    }
}
