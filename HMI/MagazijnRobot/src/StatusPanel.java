import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class StatusPanel extends JPanel implements ActionListener {
    private JLabel jlStatus;
    private static JLabel jlRobotStatus, jlRoute;
    private static boolean robotOnline;
    public StatusPanel(){
        setPreferredSize(new Dimension(400,125));
        setLayout(new FlowLayout());
        setBackground(new Color(0, 200, 200));
        setBorder(BorderFactory.createLineBorder(Color.black));

        jlStatus = new JLabel("Status", SwingConstants.CENTER);
        jlStatus.setFont(new Font("Arial", Font.BOLD, 16));
        jlStatus.setPreferredSize(new Dimension(390,16));
        jlRobotStatus = new JLabel("Robot: offline");
        jlRobotStatus.setPreferredSize(new Dimension(390,16));
        jlRoute = new JLabel("Route: geen route gevonden.");
        jlRoute.setPreferredSize(new Dimension(390,16));


        add(jlStatus);
        add(jlRobotStatus);
        add(jlRoute);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static void changeRobotStatus(boolean Online){
        robotOnline = Online;
        jlRobotStatus.setText(robotOnline == true ? "Robot: online" : "Robot: offline");
    }

    public static void displayRoute(String[] r){
        if(r != null) {
            int c = 0;
            String route = "Route: ";
            for (int i = 0; i <r.length; i++) {
                if(i%3==0){
                   c++;
                   route += (c+". ");
                }
                if((i-2)%3!=0){
                    route += (r[i] + ", ");
                } else {
                    route += (r[i] + ".  ");
                }
            }
            jlRoute.setText(route);
        } else {
            jlRoute.setText("Route: geen route gevonden.");
        }
    }
}
