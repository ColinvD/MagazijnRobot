import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderPanel extends JPanel {
    private ResultSet selectedOrder;
    private Database database;
    private JTextField jtSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;
    public OrderPanel(){
        setPreferredSize(new Dimension(300,250));
        setLayout(new FlowLayout());
        setBackground(Color.white);

        database = new Database();
        database.databaseConnect();

        jtSelectedOrder = new JTextField("Geen order geselecteerd");

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new FlowLayout());
        orderItemsPanel.setPreferredSize(new Dimension(300, 220));

        add(jtSelectedOrder);
        add(orderItemsPanel);
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
