import javax.swing.*;
import javax.swing.border.MatteBorder;
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
        setPreferredSize(new Dimension(300,400));
        setLayout(new FlowLayout());
        setBackground(new Color(159, 159, 159));
        setBorder(new MatteBorder(1, 0, 1, 1, Color.BLACK));

        database = new Database();
        database.databaseConnect();

        jlSelectedOrder = new JLabel("Geen order geselecteerd");

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new FlowLayout());
        orderItemsPanel.setPreferredSize(new Dimension(300, 420));
        orderItemsPanel.setBackground(new Color(200, 200, 200));
        orderItemsPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

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
                orderItemsPanel.add(new JLabel(itemCount + ". " + orderItems.getString("StockItemName")));
            }
        }
        if (itemCount==0){orderItemsPanel.add(new JLabel("Lege order."));}
        jlSelectedOrder.setText("Order: " + OrderID);
        this.updateUI();
    }
}
