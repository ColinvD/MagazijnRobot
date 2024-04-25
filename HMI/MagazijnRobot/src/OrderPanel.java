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
    private JLabel jlSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;
    public OrderPanel(){
        setPreferredSize(new Dimension(300,250));
        setLayout(new FlowLayout());
        setBackground(Color.white);

        database = new Database();
        database.databaseConnect();

        jlSelectedOrder = new JLabel("Geen order geselecteerd");

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new FlowLayout());
        orderItemsPanel.setPreferredSize(new Dimension(300, 220));

        add(jlSelectedOrder);
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
        if (itemCount==0){orderItemsPanel.add(new JLabel("Lege order."));}
        jlSelectedOrder.setText("Order: " + OrderID);
        this.updateUI();
    }
}
