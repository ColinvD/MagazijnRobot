import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderDialog extends JDialog implements ActionListener {
    private int orderAmount; //test orders
    private int selectedOrder = -1;
    private DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    private ArrayList<Integer> foundOrders = new ArrayList<>();
    private ArrayList<JButton> orderButtons = new ArrayList<>();
    private JButton jbCancel, jbConfirm, jbSearch;
    private JLabel jlChooseOrder, jlSearchOrder;
    private JTextField jtSearchOrder;
    private JScrollPane jsOrders;
    private JPanel jpTop, jpOrders;
    private Database database;
    private ResultSet orders;
    private OrderPanel orderPanel;
    public OrderDialog(OrderPanel orderPanel) throws SQLException {
        setSize(new Dimension(700,500));
        setTitle("Order inladen");
        setModal(true);
        this.setLayout(new GridLayout(2,1));

        this.orderPanel = orderPanel;

        jbCancel = new JButton("Annuleer");
        jbCancel.addActionListener(this);

        jbConfirm = new JButton("Bevestigen");
        jbConfirm.addActionListener(this);

        jbSearch = new JButton("Zoek");
        jbSearch.addActionListener(this);

        jlChooseOrder = new JLabel("Gekozen order: geen", SwingConstants.CENTER);
        jlChooseOrder.setPreferredSize(new Dimension(700,20));
        jlSearchOrder = new JLabel("Zoek ordernummer:");

        jtSearchOrder = new JTextField(15);
        jtSearchOrder.addActionListener(this);

        jpOrders = new JPanel();
        jpOrders.setLayout(new GridLayout(orderAmount, 3)); //maak row count het aantal orders

        jpTop = new JPanel();
        jpTop.setLayout(new FlowLayout());
        jsOrders = new JScrollPane(jpOrders);
        jsOrders.setPreferredSize(new Dimension(700,250));

        this.add(jpTop);
        this.add(jsOrders);
        jpTop.add(jbCancel);
        jpTop.add(jbConfirm);
        jpTop.add(jlChooseOrder);
        jpTop.add(jlSearchOrder);
        jpTop.add(jtSearchOrder);
        jpTop.add(jbSearch);

        database = new Database();
        database.databaseConnect();

        getOrderAmount();
        refreshData();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbCancel){
            dispose();
        }
        if (e.getSource() == jbConfirm){
            if(selectedOrder != -1){ //-1 = geen order selected
                try {
                    orderPanel.setOrder(selectedOrder);
                    System.out.println("Order inladen gelukt.");
                } catch (SQLException ex) {
                    System.out.println("Order inladen mislukt.");
                    throw new RuntimeException(ex);
                }
            }
            dispose();
        }
        if(e.getSource() == jbSearch){
            jpOrders.removeAll();
            try {
                refreshData();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            jsOrders.updateUI();
        }
        for (int i = 0; i < orderButtons.size(); i++) {
            if(e.getSource()==orderButtons.get(i)){
                selectedOrder = foundOrders.get(i);
                jlChooseOrder.setText("Gekozen order: Order " + (selectedOrder));
                jpTop.updateUI();
            }
        }
    }

    public void refreshData() throws SQLException{
        orders = database.select("SELECT * FROM orders WHERE PickingCompletedWhen IS NULL ORDER BY OrderID DESC");
        foundOrders.clear();
        orderButtons.clear();

//        jpOrders.add(new JLabel(""));
//        jpOrders.add(new JLabel("OrderID"), SwingConstants.CENTER);
//        jpOrders.add(new JLabel("Created"), SwingConstants.CENTER);
//        jpOrders.add(new JLabel("Last edited"), SwingConstants.CENTER);

        while(orders.next()){
            String orderNumber = "";
            orderNumber += orders.getInt("OrderID"); //OrderID
            if (jtSearchOrder.getText().isEmpty() || orderNumber.contains(jtSearchOrder.getText())) {
                foundOrders.add(orders.getInt("OrderID")); //OrderID
                JButton orderButton = new JButton("Selecteer");
                orderButton.addActionListener(this);
                orderButtons.add(orderButton);
                jpOrders.add(orderButton);
                String createdDate = dmy.format(orders.getDate("OrderDate"));
                String editedDate = dmy.format(orders.getDate("LastEditedWhen"));
                jpOrders.add(new JLabel("Order: " + orders.getInt("OrderID"), SwingConstants.CENTER));
                jpOrders.add(new JLabel(createdDate, SwingConstants.CENTER));
                jpOrders.add(new JLabel(editedDate, SwingConstants.CENTER));
            }
        }
        jpOrders.setLayout(new GridLayout(foundOrders.size(),3));
    }
    public void getOrderAmount() throws SQLException {
        ResultSet rs = database.select("Select COUNT(*) FROM orders WHERE PickingCompletedWhen IS NULL ORDER BY OrderID DESC");
        rs.next();
        orderAmount = rs.getInt(1);
    }
}
