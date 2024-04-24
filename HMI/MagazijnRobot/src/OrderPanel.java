import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderPanel extends JPanel implements ActionListener {
    private JButton jbOrder;
    private ResultSet selectedOrder;
    private Database database;
    private JTextField jtSelectedOrder;
    public OrderPanel(){
        setPreferredSize(new Dimension(200,400));
        setLayout(new FlowLayout());
        setBackground(Color.white);

        database = new Database();
        database.databaseConnect();

        jbOrder = new JButton("Inladen order");
        jbOrder.addActionListener(this);

        jtSelectedOrder = new JTextField("Geen order geselecteerd");

        add(jbOrder);
        add(jtSelectedOrder);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbOrder){
            try {
                OrderDialog dialog = new OrderDialog(this);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void setOrder(int OrderID) throws SQLException {
        selectedOrder = database.getOrder(OrderID);
        jtSelectedOrder.setText("Order: " + OrderID);
        database.printResult(selectedOrder);
        this.updateUI();
    }
}
