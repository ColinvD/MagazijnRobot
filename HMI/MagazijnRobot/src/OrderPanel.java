import com.mysql.cj.x.protobuf.MysqlxCrud;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderPanel extends JPanel implements ActionListener, Listener {
    private int k = 0;
    private int m = 0;

    // SerialCommunicator serialCommunicator = HMIScreen.serialCommunicator;
    ArrayList<ArrayList<Locatie>> Boxes;
    private Database database;
    private JLabel jlSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;

    private ArrayList<JLabel> products1;

    private JButton jbStartOrder;
    private JScrollPane orderJSP;

    private int order;
    int sizebox;

    private int counterChangeAmountColor;
    private int counterMaxAmountColor;


    private ArrayList<Integer> orderlinesIdSorted;

    public OrderPanel() {
        // serialCommunicator.AddListener(this);
        setPreferredSize(new Dimension(400, 400));
        setLayout(new FlowLayout());
        setBackground(new Color(159, 159, 159));
        setBorder(new MatteBorder(1, 0, 1, 1, Color.BLACK));
        database = new Database();
        database.databaseConnect();

        jlSelectedOrder = new JLabel("Geen order geselecteerd");

        jbStartOrder = new JButton("Start");
        jbStartOrder.setPreferredSize(new Dimension(100, 30));
        jbStartOrder.addActionListener(this);

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
        orderItemsPanel.setBackground(new Color(200, 200, 200));
        orderItemsPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

        orderJSP = new JScrollPane(orderItemsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        orderJSP.setPreferredSize(new Dimension(400, 360));
        add(jlSelectedOrder);
        add(jbStartOrder);
        add(orderJSP);
    }

    public void setOrder(int OrderID) throws SQLException {
        order = OrderID;
        products1 = new ArrayList<>();
        orderlinesIdSorted = new ArrayList<>();
        ResultSet selectedOrder = database.getOrder(OrderID);
        database.printResult(selectedOrder);
        orderItems = database.getOrderlines(OrderID);
        orderItemsPanel.removeAll();
        int itemCount = database.getOrderSize(OrderID);

        if (itemCount == 0) {
            orderItemsPanel.add(new JLabel("Lege order."));
            SchapPanel.drawRoute(null);
            StatusPanel.displayRoute(null);
        } else {
            ArrayList<Locatie> products = new ArrayList<>();
            while (orderItems.next()) {
                Locatie locatie = new Locatie(orderItems.getString("StockLocation"), orderItems.getInt("Weight"), orderItems.getInt("OrderLineID"));
                for (int i = 0; i < orderItems.getInt("Quantity"); i++) {
                    products.add(locatie);
                }
            }
            Boxes = BBP.firstFitDec(products, products.size(), 10);

            //This only shows the first box of the order

            ArrayList<Locatie> box = new ArrayList<>(Boxes.getFirst());

            ArrayList<Locatie> route = TSP.getRoute(box);
            StatusPanel.displayRoute(route);
            SchapPanel.drawRoute(route);
            // orderItems = database.getOrderlines(OrderID);
            for (int i = 0; i < Boxes.size(); i++) {
                JLabel doos = new JLabel("Doos " + (i + 1) + ": ");
                doos.setFont(new Font("Arial", Font.BOLD, 16));
                if (i > 0) {
                    doos.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                }
                products1.add(doos);
                orderItemsPanel.add(doos);
                for (int j = 0; j < Boxes.get(i).size(); j++) {
                    String location = Boxes.get(i).get(j).getLocation();
                    ResultSet stockitem = database.getStockitem(location);
                    JLabel product = new JLabel(location + ": " + stockitem.getInt("StockItemID") + ". " + stockitem.getString("StockItemName") + ". " + stockitem.getString("Weight") + ".");
                    products1.add(product);
                    orderlinesIdSorted.add(Boxes.get(i).get(j).getOrderlineID());


//             while (orderItems.next()) {
//                 for (int i = 0; i < orderItems.getInt("Quantity"); i++) {

//                     String Stocklocation = orderItems.getString("StockLocation");
//                     if (Stocklocation == null){
//                         Stocklocation = "Niet aanwezig";
//                     }
//                     JLabel product = new JLabel(orderItems.getInt("StockItemID") + ". " + orderItems.getString("StockItemName")  + ".  "+Stocklocation);
//                     product.setPreferredSize(new Dimension(280, 12));
                    orderItemsPanel.add(product);
                }
            }
        }
        jlSelectedOrder.setText("Order: " + OrderID + " ");
        //  counterMaxAmountColor = 0;
        m = 0;
        k = 0;
        this.updateUI();
    }

    public void changeOrderColor() throws SQLException {
        int l = 0;
        orderItemsPanel.removeAll();
        sizebox = Boxes.get(m).size();
        if (k == sizebox) {
            m++;
            k = 0;
            sizebox = Boxes.get(m).size();
        }
        ArrayList<Locatie> locaties = new ArrayList<>();
        counterChangeAmountColor = 0;
        for (int i = 0; i < sizebox; i++) {
            locaties.add(Boxes.get(m).get(i));
        }
        // System.out.println(Arrays.toString(box));
        ArrayList<Locatie> route = TSP.getRoute(locaties);
        System.out.println(route);
        database.updatepicked(route.get(k).getOrderlineID());
        database.updateOrderlineAfterOrder(route.get(k).getOrderlineID());
        int doosCount = 0;
        if (k == 1 && sizebox > 2) {
            l = 1; // idk why this but it works for now. (get back to it later)
        }
        for (JLabel label : products1) {
            if (!label.getText().contains("Doos")) {
                if (label.getText().substring(0, 2).equals(route.get(k).getLocation()) && m + 1 == doosCount) {
                    if (l <= k) {
                        label.setForeground(Color.GREEN);
                        l++;
                    }
                }
            } else {
                doosCount++;
            }
            orderItemsPanel.add(label);
        }
        k++;
        updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbStartOrder) {
//                    try {
//                        serialCommunicator.sendMessageToArduino("Start");
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//            try {
//                counterMaxAmountColor++;
//                changeOrderColor();
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
        }

    }


    @Override
    public void onMessageReceived(String message) throws SQLException {
        if (message.equals("Out")) {
            System.out.println("out");
            counterMaxAmountColor++;
            changeOrderColor();
        }
        if (message.equals("Complete")) {
            System.out.println("complete");
            database.updatePickedOrder(order);
            JOptionPane.showMessageDialog(this, "De order is afgerond");
        }
    }
}
