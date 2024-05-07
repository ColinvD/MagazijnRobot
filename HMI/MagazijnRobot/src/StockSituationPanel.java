import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class StockSituationPanel extends JPanel implements ActionListener {
    private static JLabel positie = new JLabel();
    private static JLabel name, quantity;
    private static Grid schap;
    public StockSituationPanel(Grid schap) {
        StockSituationPanel.schap = schap;
        setPreferredSize(new Dimension(400,75));
        setBackground(Color.white);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));
        name = new JLabel();
        quantity = new JLabel();
        positie.setPreferredSize(new Dimension(400,50)) ;
        name.setPreferredSize(new Dimension(400,50)) ;
        quantity.setPreferredSize(new Dimension(400,50)) ;

        //positie.setText("Plek " + schap.getNamePositie());
        add(positie);
        add(name);
        add(quantity);
    }
    public static void changePosition() throws SQLException {
        Database database = new Database();
        database.databaseConnect();
        ArrayList<String> results = database.getStockInfo(schap.getNamePositie());
        if (!results.isEmpty()){
            name.setText("naam: " + results.get(0));
            quantity.setText("voorraad: " + results.get(1));
        } else {
            name.setText("");
            quantity.setText("");
        }
        positie.setText("Plek: " + schap.getNamePositie()) ;

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
}

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}
