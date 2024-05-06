import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusPanel extends JPanel implements ActionListener {
    public StatusPanel(){
        setPreferredSize(new Dimension(400,200));
        setLayout(new FlowLayout());
        setBackground(new Color(16, 70, 97));
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
