import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderPanel extends JPanel implements ActionListener {
    private JButton jbOrder;
    private ResultSet selectedOrder;
    private Database database;
    private JTextField jtSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;
    public OrderPanel(){
        setPreferredSize(new Dimension(300,400));
        setLayout(new FlowLayout());
        setBackground(Color.white);

        database = new Database();
        database.databaseConnect();

        jbOrder = new JButton("Inladen order");
        jbOrder.addActionListener(this);

        jtSelectedOrder = new JTextField("Geen order geselecteerd");

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new FlowLayout());
        orderItemsPanel.setPreferredSize(new Dimension(300, 370));

        add(jbOrder);
        add(jtSelectedOrder);
        add(orderItemsPanel);
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
        database.printResult(selectedOrder);
        orderItems = database.getOrderlines(OrderID);
        orderItemsPanel.removeAll();
        int itemCount = 0;
        while (orderItems.next()){
            for (int i = 0; i < orderItems.getInt("Quantity"); i++) {
                itemCount++;
                orderItemsPanel.add(new JLabel(itemCount + ". " + orderItems.getString("StockItemName") + ", " + orderItems.getString("OrderStatus")));
            }
        }
        jtSelectedOrder.setText("Order: " + OrderID);
        this.updateUI();
    }
}
