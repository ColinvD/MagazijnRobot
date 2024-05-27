import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class SchapPanel extends JPanel implements ActionListener {
    private static String[] route;
    private static Grid schap;
    private JLabel schapNaam = new JLabel("Schap");
    private JButton voorraadstiuatieWijzigen = new JButton("Voorraadstiuatie wijzigen");

    public SchapPanel() throws SQLException {

        voorraadstiuatieWijzigen.setFocusable(false);
        setPreferredSize(new Dimension(400, 475));
        setLayout(new FlowLayout());
        schapNaam.setFont(new Font("default", Font.BOLD, 20));
        voorraadstiuatieWijzigen.setPreferredSize(new Dimension(350, 30));
        setBackground(new Color(247, 238, 115));
        setBorder(BorderFactory.createLineBorder(Color.black));
        schap = new Grid(5, 5);
        LegendaPanel legendaPanel = new LegendaPanel();
        StockSituationPanel stockSituationPanel = new StockSituationPanel(schap);
        voorraadstiuatieWijzigen.addActionListener(this);
        add(schapNaam);
        add(voorraadstiuatieWijzigen);
        add(schap);
        add(stockSituationPanel);
        add(legendaPanel);
        add(stockSituationPanel);
        addStockitems();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == voorraadstiuatieWijzigen) {
            StockSituationDialog stockSituationDialog = new StockSituationDialog(schap);
        }
    }

    public static void addStockitems() throws SQLException {
        Database database = new Database();
        for (String s : database.getOrders()) {
            for (int i = 0; i < schap.getGrid().length; i++) {
                for (int j = 0; j < schap.getGrid().length; j++) {
                    GridSpace gridcell = schap.getGrid()[i][j];
                    if (s != null) {
                        if (gridcell.getGridText().equals(s)) {
                            gridcell.setBackgroundColor(Color.black);
                        }
                    } else gridcell.setBackgroundColor(Color.gray);

                }
            }
        }
        schap.repaint();
    }

    public static void drawRoute(ArrayList<Locatie> r){
        schap.setRoute(r);
        schap.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
