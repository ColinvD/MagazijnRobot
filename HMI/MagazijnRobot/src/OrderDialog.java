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
    private JPanel jpA, jpOrders;
    private Database database;
    private ResultSet orders;
    public OrderDialog() throws SQLException {
        setSize(new Dimension(700,500));
        setTitle("Order inladen");
        setModal(true);
        this.setLayout(new GridLayout(2,1));

        jbCancel = new JButton("Annuleer");
        jbCancel.addActionListener(this);

        jbConfirm = new JButton("Bevestigen");
        jbConfirm.addActionListener(this);

        jbSearch = new JButton("Zoek");
        jbSearch.addActionListener(this);

        jlChooseOrder = new JLabel("Kies order om in te laden");
        jlSearchOrder = new JLabel("Zoek ordernummer:");

        jtSearchOrder = new JTextField(15);
        jtSearchOrder.addActionListener(this);

        jpOrders = new JPanel();
        jpOrders.setLayout(new GridLayout(orderAmount, 3)); //maak row count het aantal orders

        jpA = new JPanel();
        jpA.setLayout(new FlowLayout());
        jsOrders = new JScrollPane(jpOrders);
        jsOrders.setPreferredSize(new Dimension(700,250));

        this.add(jpA);
        this.add(jsOrders);
        jpA.add(jbCancel);
        jpA.add(jbConfirm);
        jpA.add(jlChooseOrder);
        jpA.add(jlSearchOrder);
        jpA.add(jtSearchOrder);
        jpA.add(jbSearch);

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
            //Maak code om order door te geven
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
                jpA.updateUI();
            }
        }
    }

    public void refreshData() throws SQLException{
        orders = database.select("SELECT * FROM orders");
        foundOrders.clear();
        orderButtons.clear();

        while(orders.next()){
            String orderNumber = "";
            orderNumber += orders.getInt(1);
            if (jtSearchOrder.getText().isEmpty() || orderNumber.contains(jtSearchOrder.getText())) {
                foundOrders.add(orders.getInt(1));
                JButton orderButton = new JButton("Selecteer");
                orderButton.addActionListener(this);
                orderButtons.add(orderButton);
                jpOrders.add(orderButton);
                String createdDate = dmy.format(orders.getDate(2));
                String editedDate = dmy.format(orders.getDate(3));
                jpOrders.add(new JLabel("Order: " + orders.getInt(1)));
                jpOrders.add(new JLabel(createdDate));
                jpOrders.add(new JLabel(editedDate));
            }
        }
        jpOrders.setLayout(new GridLayout(foundOrders.size(),3));
    }
    public void getOrderAmount() throws SQLException {
        ResultSet rs = database.select("Select COUNT(*) FROM orders");
        rs.next();
        orderAmount = rs.getInt(1);
    }
}
