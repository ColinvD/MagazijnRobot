import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderPanel extends JPanel implements ActionListener {
    private ResultSet selectedOrder;
    private Database database;
    private JLabel jlSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;
    private JButton jbStartOrder;
    private JScrollPane orderJSP;
    public OrderPanel(){
        setPreferredSize(new Dimension(300,400));
        setLayout(new FlowLayout());
        setBackground(new Color(159, 159, 159));
        setBorder(new MatteBorder(1, 0, 1, 1, Color.BLACK));

        database = new Database();
        database.databaseConnect();

        jlSelectedOrder = new JLabel("Geen order geselecteerd ");

        jbStartOrder = new JButton("Start");
        jbStartOrder.setPreferredSize(new Dimension(100,30));
        jbStartOrder.addActionListener(this);

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
        orderItemsPanel.setBackground(new Color(200, 200, 200));
        orderItemsPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

        orderJSP = new JScrollPane(orderItemsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        orderJSP.setPreferredSize(new Dimension(300,360));
        add(jlSelectedOrder);
        add(jbStartOrder);
        add(orderJSP);
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
                JLabel product = new JLabel(orderItems.getInt("StockItemID") + ". " + orderItems.getString("StockItemName"));
                product.setPreferredSize(new Dimension(280,15));

                orderItemsPanel.add(product);
            }
        }

        if (itemCount==0){
            orderItemsPanel.add(new JLabel("Lege order."));
        }

        jlSelectedOrder.setText("Order: " + OrderID + " ");
        this.updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
