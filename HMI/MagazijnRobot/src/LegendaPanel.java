import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LegendaPanel extends JPanel implements ActionListener {
    private JLabel voorraadVol = new JLabel("Schap Vol:");

    private JLabel voorraadLeeg = new JLabel("Schap leeg:");
    private JLabel locatieRobot = new JLabel("Locatie Robot:");
    private JLabel out = new JLabel("Uitstrekken:");
    private JLabel noodstop = new JLabel("In noodstop:");

    public LegendaPanel(){
        setPreferredSize(new Dimension(358,45));
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        setBackground(new Color(247, 238, 115));
        add(locatieRobot);
        add(out);
        add(noodstop);
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
        g.fillRect(255,30,10,10);
        g.setColor(Color.white);
        g.drawRect(255,30,10,10);
        g.setColor(Color.gray);
        g.fillRect(177,30,10,10);
        g.setColor(Color.white);
        g.drawRect(177,30,10,10);
        g.setColor(Color.GREEN);
        g.fillOval(135,9,12,12);
        g.setColor(Color.ORANGE);
        g.fillOval(223,9,12,12);
        g.setColor(Color.RED);
        g.fillOval(313,9,12,12);


    }

}
