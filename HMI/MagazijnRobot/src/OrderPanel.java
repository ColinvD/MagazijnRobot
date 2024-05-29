import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderPanel extends JPanel implements ActionListener, Listener {


    private int routepoint = 0;
    private int doos = 0;

    private int counterBoxes = 0;

    SerialCommunicator serialCommunicator = HMIScreen.serialCommunicator;
    private ArrayList<ArrayList<Locatie>> Boxes;
    private ResultSet selectedOrder;
    private int selectedOrderID;
    private Database database;
    private JLabel jlSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;

    private ArrayList<JLabel> products1;

    private JButton jbStartOrder;
    private JScrollPane orderJSP;

    //private int order;
    private int sizebox;

    private int counterChangeAmountColor;
    private int counterMaxAmountColor;

    public OrderPanel() {
        // serialCommunicator.AddListener(this);
        setPreferredSize(new Dimension(400, 450));
        setLayout(new FlowLayout());
        setBackground(new Color(159, 159, 159));
        setBorder(new MatteBorder(1, 0, 1, 1, Color.BLACK));
        database = new Database();
        database.databaseConnect();

        jlSelectedOrder = new JLabel("Geen order geselecteerd");

        jbStartOrder = new JButton("Start");
        jbStartOrder.setPreferredSize(new Dimension(100, 30));
        jbStartOrder.addActionListener(this);
        jbStartOrder.setFocusable(false);

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
        orderItemsPanel.setBackground(new Color(200, 200, 200));
        orderItemsPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

        orderJSP = new JScrollPane(orderItemsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        orderJSP.setPreferredSize(new Dimension(400, 410));
        orderJSP.getVerticalScrollBar().setUnitIncrement(17);
        jbStartOrder.setEnabled(false);
        add(jlSelectedOrder);
        add(jbStartOrder);
        add(orderJSP);
    }

    public void setOrder(int OrderID) throws SQLException {
        products1 = new ArrayList<>();
        this.selectedOrderID = OrderID;
        selectedOrder = database.getOrder(OrderID);
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

                try {
                    ArrayList<Locatie> box1 = new ArrayList<>(Boxes.get(i));
                    ArrayList<Locatie> routes = TSP.getRoute(box1);
                    // System.out.println(Arrays.toString(box));
                    ArrayList<Locatie> locaties = new ArrayList<>(box1.size());
                    for (int q = 0; q < routes.size(); q++) {
                        System.out.println(i);
                        for (int l = 0; l < box1.size(); l++) {
                            if (routes.get(q).getLocation().equals(box1.get(l).getLocation())) {
                                locaties.add(box1.get(l));
                                break;
                            }
                        }
                    }
                    Boxes.set(i, locaties);
                } catch (NullPointerException w){
                    System.out.println("test");
                }

                for (int j = 0; j < Boxes.get(i).size(); j++) {
                        String location = Boxes.get(i).get(j).getLocation();
                        ResultSet stockitem = null;
                        JLabel product = null;
                        try {
                            stockitem = database.getStockitem(location);
                            product = new JLabel(location + ": " + stockitem.getInt("StockItemID") + ". " + stockitem.getString("StockItemName") + ". " + stockitem.getString("Weight") + ".");
                        } catch (SQLException e){
                            System.out.println();
                            product = new JLabel("Geen Locatie: " + database.selectEmptyLocation(Boxes.get(i).get(j).getOrderlineID()));
                        }
                        products1.add(product);
                        orderItemsPanel.add(product);
                }
            }
        }
        jlSelectedOrder.setText("Order: " + OrderID + " ");
        doos = 0;
        routepoint = 0;
        counterMaxAmountColor = 0;
        jbStartOrder.setEnabled(true);
        ButtonPanel.SetUpdateButtonEnabled(true);
        ButtonPanel.SetPackingButtonEnabled(true);
        this.updateUI();
    }

    public void changeOrderColor() throws SQLException {
        counterChangeAmountColor = 0;
        orderItemsPanel.removeAll();
        ArrayList<Locatie> locaties = new ArrayList<>();
        try {
            sizebox = Boxes.get(doos).size();
            if (routepoint == sizebox) {
                doos++;
                routepoint = 0;
                sizebox = Boxes.get(doos).size();
            }
            for (int i = 0; i < sizebox; i++) {
                locaties.add(Boxes.get(doos).get(i));
            }
        } catch (IndexOutOfBoundsException e){
            System.out.println();
        }
        // System.out.println(Arrays.toString(box));
        ArrayList<Locatie> route = TSP.getRoute(locaties);
        System.out.println(route);
        database.updatepicked(route.get(routepoint).getOrderlineID());
        database.updateOrderlineAfterOrder(route.get(routepoint).getOrderlineID());
            for (JLabel label : products1) {
                if (!label.getText().contains("Doos")) {
                    if (counterChangeAmountColor <= counterMaxAmountColor) {
                        counterChangeAmountColor++;
                        label.setForeground(Color.GREEN);

                    }
                }
                orderItemsPanel.add(label);
            }
        routepoint++;
        updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbStartOrder) {
                    try {
                        serialCommunicator.sendMessageToArduino("Start");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
//            try {
//                changeOrderColor();
//                counterMaxAmountColor++;
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
        }

    }


    @Override
    public void onMessageReceived(String message) throws SQLException {
        if (message.equals("Out")) {
            System.out.println("out");
            changeOrderColor();
            counterMaxAmountColor++;
        }
        if (message.equals("Complete")) {
            System.out.println("complete");
            database.updatePickedOrder(selectedOrderID);
            JOptionPane.showMessageDialog(this, "De order is afgerond");
        }
        if (message.equals("Done")) {
            System.out.println("Done");
            counterBoxes++;
            ArrayList<Locatie> box = new ArrayList<>(Boxes.get(counterBoxes));
            ArrayList<Locatie> route = TSP.getRoute(box);
            StatusPanel.displayRoute(route);
            SchapPanel.drawRoute(route);

        }

    }

    public int getSelectedOrderID() {
        return selectedOrderID;
    }

    public void refreshOrder() throws SQLException {
        if(selectedOrderID > 0) {
            setOrder(this.selectedOrderID);
        }
    }
    public ArrayList<JLabel> getBoxes(){
        return products1;
    }
}
