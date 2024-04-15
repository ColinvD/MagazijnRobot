import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchapPanel extends JPanel implements ActionListener {
    public SchapPanel(){
        setPreferredSize(new Dimension(400,400));
        setLayout(new FlowLayout());
        setBackground(Color.red);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
