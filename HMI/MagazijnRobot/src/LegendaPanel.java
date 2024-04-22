import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LegendaPanel extends JPanel implements ActionListener {
    private JLabel voorraadVol = new JLabel("Schap Vol:");

    private JLabel voorraadLeeg = new JLabel("Schap leeg:");
    private JLabel locatieRobot = new JLabel("Locatie Robot:");
    public LegendaPanel(){
        setPreferredSize(new Dimension(400,400));
        setLayout(new FlowLayout(FlowLayout.LEFT, 40, 0));
        setBackground(Color.RED);
        add(locatieRobot);
        add(voorraadLeeg);
        add(voorraadVol);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(335,5,10,10);
        g.setColor(Color.white);
        g.drawRect(335,5,10,10);
        g.setColor(Color.gray);
        g.fillRect(235,5,10,10);
        g.setColor(Color.white);
        g.drawRect(235,5,10,10);
        g.setColor(Color.GREEN);
        g.fillOval(130,4,12,12);
        g.setColor(Color.white);
        g.drawOval(130,4,12,12);


    }

}
