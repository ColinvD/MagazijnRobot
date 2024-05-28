import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;

public class HMIScreen extends JFrame implements ActionListener,Listener {
    private static String location;
    public static int orderid = -1;
    private boolean nood = false;
    private String status;
    OrderPanel order = new OrderPanel();
    public static int counter;
     public static SerialCommunicator serialCommunicator = new SerialCommunicator("COM6", 9600);
    public HMIScreen() throws SQLException, IOException, InterruptedException {
    //        serialCommunicator.AddListener(this);
            setSize(900, 650);
            getContentPane().setBackground(Color.black);
            setTitle("HMI magazijn robot");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            SchapPanel schap = new SchapPanel(order);
            StatusPanel status = new StatusPanel();
            StatusPanel.changeRobotStatus(true);
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
//        if (!message.isEmpty()){
//            Grid.locationRobot2 = (message.substring(0,message.indexOf(',')));
//            status =  message.substring(message.indexOf(','));
//        }
        if (message.equals("STOP")){
            System.out.println("STO");
            JPopupMenu jPopupMenu = new JPopupMenu();
            jPopupMenu.setLabel("Noodstop is ingedrukt");

        }
        if (message.equals("Unlock")){
            JPopupMenu jPopupMenu = new JPopupMenu();
            jPopupMenu.setLabel("Noodstop is ontgrendeld");

        }
    }


}
