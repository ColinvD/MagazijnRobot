import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HMIScreen extends JFrame implements ActionListener {
    public HMIScreen(){
        setSize(800,800);
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.black);
        setTitle("HMI magazijn robot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SchapPanel schap = new SchapPanel();
        ButtonPanel order = new ButtonPanel();
        StatusPanel status = new StatusPanel();
        add(schap);
        add(order);
        add(status);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
