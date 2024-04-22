import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchapPanel extends JPanel implements ActionListener{
    private Grid schap;
    private JLabel schapNaam = new JLabel("Schap");
    private JButton voorraadstiuatieWijzigen = new JButton("Voorraadstiuatie wijzigen");

    public SchapPanel(){
        voorraadstiuatieWijzigen.setFocusable(false);
        setPreferredSize(new Dimension(400,400));
        setLayout(new FlowLayout());
        schapNaam.setFont(new Font("default",Font.BOLD,20));
        voorraadstiuatieWijzigen.setPreferredSize(new Dimension(350,30));
        setBackground(Color.red);
        schap = new Grid(5,5);
        LegendaPanel legendaPanel = new LegendaPanel();
        add(schapNaam);
        add(voorraadstiuatieWijzigen);
        add(schap);
        add(legendaPanel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}

