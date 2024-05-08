import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class StatusPanel extends JPanel implements ActionListener {
    private JLabel jlStatus, jlRobotStatus;
    private boolean robotOnline;
    private JButton jbTest;
    public StatusPanel(){
        setPreferredSize(new Dimension(400,125));
        setLayout(new FlowLayout());
        setBackground(new Color(0, 200, 200));
        setBorder(BorderFactory.createLineBorder(Color.black));

        jlStatus = new JLabel("Status", SwingConstants.CENTER);
        jlStatus.setPreferredSize(new Dimension(400,25));
        jlRobotStatus = new JLabel("Robot: offline");
        jbTest = new JButton("Robot status veranderen");
        jbTest.addActionListener(this);

        add(jlStatus);
        add(jlRobotStatus);
        add(jbTest);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==jbTest){
            changeRobotStatus(!robotOnline);
        }
    }

    public void changeRobotStatus(boolean Online){
        this.robotOnline = Online;
        jlRobotStatus.setText(robotOnline == true ? "Robot: online" : "Robot: offline");
        this.updateUI();
    }
}
