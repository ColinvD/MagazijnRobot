import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchapPanel extends JPanel implements ActionListener {
    private Grid schap;
    public SchapPanel(){
        setPreferredSize(new Dimension(400,400));
        setLayout(new FlowLayout());
        setBackground(Color.red);
        schap = new Grid(5,5);
        add(schap);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
