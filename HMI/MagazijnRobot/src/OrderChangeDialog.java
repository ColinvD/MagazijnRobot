import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.*;

public class OrderChangeDialog extends JDialog implements ActionListener {
    private int orderAmount; //test orders
    private int selectedOrder = -1;
    private ArrayList<ArrayList> listOrder;
    private DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    private ArrayList<Integer> orderLines = new ArrayList<>();
    private ArrayList<JSpinner> orderlinesSpinners = new ArrayList<>();
    private JButton jbCancel, jbConfirm;
    private JLabel jlChooseOrder;
    private JScrollPane jsOrders;
    private JPanel jpTop, jpOrderlines;
    private Database database;
    private OrderPanel orderPanel;
    private ResultSet order;
    public OrderChangeDialog(OrderPanel orderPanel) throws SQLException {
        setSize(new Dimension(700,350));
        setTitle("Order Bewerken");
        setModal(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        database = new Database();
        database.databaseConnect();

        this.orderPanel = orderPanel;
        this.order = database.getOrderlines(orderPanel.getSelectedOrderID());
        this.listOrder = new ArrayList<>();

        // Haal de ResultSetMetaData op om dynamisch kolomnamen en types te krijgen
        ResultSetMetaData metaData = order.getMetaData();
        int columnCount = metaData.getColumnCount();
        while(order.next()){
            ArrayList<DatabaseValue> row = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = order.getObject(i);

                DatabaseValue newValue = new DatabaseValue(columnName, columnValue);
                row.add(newValue);
            }

            listOrder.add(row);
        }
        //System.out.println(listOrder);
        //this.order.next();
        //System.out.println(order.getInt("OrderID"));
        //this.order = orderPanel.getSecondOrderSet();
        //database.printResult(this.order);
        //order.beforeFirst();
        //System.out.println(order.getMetaData().getColumnLabel(1) + order.getString(1));

        jbCancel = new JButton("Annuleer");
        jbCancel.addActionListener(this);

        jbConfirm = new JButton("Bevestigen");
        jbConfirm.addActionListener(this);

        //jbSearch = new JButton("Zoek");
        //jbSearch.addActionListener(this);

        //jlChooseOrder = new JLabel("Gekozen order: geen", SwingConstants.CENTER);
        //System.out.println(listOrder.getFirst().getFirst());
        DatabaseValue temp  = (DatabaseValue) listOrder.getFirst().getFirst();

        jlChooseOrder = new JLabel("Order: " + temp.getValue(), SwingConstants.CENTER);
        jlChooseOrder.setPreferredSize(new Dimension(700,20));
        //jlSearchOrder = new JLabel("Zoek ordernummer:");

        //jtSearchOrder = new JTextField(15);
        //jtSearchOrder.addActionListener(this);

        jpOrderlines = new JPanel();
        jpOrderlines.setLayout(new GridBagLayout());
        //jpOrderlines.setLayout(new GridLayout(listOrder.size(), 4)); //maak row count het aantal orders

        jpTop = new JPanel();
        jpTop.setLayout(new FlowLayout());

        jsOrders = new JScrollPane(jpOrderlines);
        jsOrders.setPreferredSize(new Dimension(700,250));


        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        this.add(jpTop, c);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 3;
        this.add(jsOrders, c);
        jpTop.add(jbCancel);
        jpTop.add(jbConfirm);
        jpTop.add(jlChooseOrder);
        //jpTop.add(jlSearchOrder);
        //jpTop.add(jtSearchOrder);
        //jpTop.add(jbSearch);


        //getOrderAmount();
        refreshData();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbCancel){
            closeDatabase();
            dispose();
        }
        /*if (e.getSource() == jbConfirm){
            if(selectedOrder != -1){ //-1 = geen order selected
                try {
                    orderPanel.setOrder(selectedOrder);
                    System.out.println("Order inladen gelukt.");
                } catch (SQLException ex) {
                    System.out.println("Order inladen mislukt.");
                    throw new RuntimeException(ex);
                }
            }
            closeDatabase();
            dispose();
        }
        if(e.getSource() == jbSearch){
            jpOrderlines.removeAll();
            try {
                refreshData();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            jsOrders.updateUI();
        }
        for (int i = 0; i < orderlinesButtons.size(); i++) {
            if(e.getSource()==orderlinesButtons.get(i)){
                selectedOrder = orderLines.get(i);
                jlChooseOrder.setText("Gekozen order: Order " + (selectedOrder));
                jpTop.updateUI();
            }
        }*/
    }

    public void refreshData(){
        orderLines.clear();
        orderlinesSpinners.clear();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(5,5,0,5);
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        jpOrderlines.add(new JLabel("Artikel Nummer", SwingConstants.CENTER), c);
        c.gridx = 1;
        jpOrderlines.add(new JLabel("Product Naam", SwingConstants.CENTER), c);
        c.gridx = 2;
        jpOrderlines.add(new JLabel("Aantal", SwingConstants.CENTER), c);

        for (int i  = 0; i< listOrder.size(); i++) {
            ArrayList<DatabaseValue> orderline = listOrder.get(i);
            orderLines.add((int) orderline.get(2).getValue());
            int productID = 0;
            String itemName = "";
            int itemQuantity = 0;
            for (DatabaseValue databaseValue : orderline) {
                switch (databaseValue.getColomn()) {
                    case "StockItemID":
                        productID = (int) databaseValue.getValue();
                        break;
                    case "Description": 
                        itemName = (String) databaseValue.getValue();
                        break;
                    case "Quantity":
                        itemQuantity = (int) databaseValue.getValue();
                        break;
                    default:
                        break;
                }
            }
            c.gridx = 0;
            c.gridy = i+1;
            jpOrderlines.add(new JLabel("" + productID, SwingConstants.CENTER), c);
            c.gridx = 1;
            jpOrderlines.add(new JLabel(itemName, SwingConstants.CENTER), c);
//            c.gridx = 2;
//            jpOrderlines.add(new JLabel("" + itemQuantity, SwingConstants.CENTER), c);

            JSpinner quantity = new JSpinner();
            quantity.setPreferredSize(new Dimension(50,20));
            quantity.setValue(itemQuantity);
            c.gridx = 2;
            orderlinesSpinners.add(quantity);
            jpOrderlines.add(quantity, c);
//            JButton increaseButton = new JButton("+");
//            increaseButton.addActionListener(this);
//            orderlinesButtons.add(increaseButton);
//            JButton decreaseButton = new JButton("-");
//            decreaseButton.addActionListener(this);
//            orderlinesButtons.add(decreaseButton);
//            c.gridx = 3;
//            jpOrderlines.add(increaseButton, c);
//            c.gridx = 4;
//            jpOrderlines.add(decreaseButton, c);
        }

        if(orderLines.isEmpty()) {
            jpOrderlines.removeAll();
            jpOrderlines.setLayout(new GridLayout(1,1));
            jpOrderlines.add(new JLabel("Geen orders gevonden.", SwingConstants.CENTER));
        }
    }

    public void closeDatabase(){
        try{
            database.close();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}
