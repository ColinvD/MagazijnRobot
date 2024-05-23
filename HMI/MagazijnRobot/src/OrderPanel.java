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

public class OrderPanel extends JPanel implements ActionListener,Listener {
    //SerialCommunicator serialCommunicator = new SerialCommunicator("COM4",500000);
    private boolean firstTimeGoingOut = true;
    private ResultSet selectedOrder;
    private Database database;
    private JLabel jlSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;

    private ArrayList<JLabel> products1;

    private JButton jbStartOrder;
    private JScrollPane orderJSP;

    private int order;

    private int counter;
    private int counter1;



    private ArrayList<Integer> orderlinesIdSorted;
    public OrderPanel(){
        // serialCommunicator.AddListener(this);
        setPreferredSize(new Dimension(400,400));
        setLayout(new FlowLayout());
        setBackground(new Color(159, 159, 159));
        setBorder(new MatteBorder(1, 0, 1, 1, Color.BLACK));

        database = new Database();
        database.databaseConnect();

        jlSelectedOrder = new JLabel("Geen order geselecteerd");

        jbStartOrder = new JButton("Start");
        jbStartOrder.setPreferredSize(new Dimension(100,30));
        jbStartOrder.addActionListener(this);

        orderItemsPanel = new JPanel();
        orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
        orderItemsPanel.setBackground(new Color(200, 200, 200));
        orderItemsPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));

        orderJSP = new JScrollPane(orderItemsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        orderJSP.setPreferredSize(new Dimension(400,360));
        add(jlSelectedOrder);
        add(jbStartOrder);
        add(orderJSP);
    }
    public void setOrder(int OrderID) throws SQLException {
        order = OrderID;
        products1 = new ArrayList<>();
        orderlinesIdSorted = new ArrayList<>();
        selectedOrder = database.getOrder(OrderID);
        database.printResult(selectedOrder);
        orderItems = database.getOrderlines(OrderID);
        orderItemsPanel.removeAll();
        int itemCount = database.getOrderSize(OrderID);

        if(itemCount==0){
            orderItemsPanel.add(new JLabel("Lege order."));
            SchapPanel.drawRoute(null);
            StatusPanel.displayRoute(null);
        } else {
            ArrayList<Locatie> products = new ArrayList<>();
            while (orderItems.next()){
                Locatie locatie = new Locatie(orderItems.getString("StockLocation"), orderItems.getInt("Weight"),orderItems.getInt("OrderLineID"));
                for(int i = 0; i < orderItems.getInt("Quantity"); i++){
                    products.add(locatie);
                }
            }
            ArrayList<ArrayList<Locatie>> Boxes = BBP.firstFitDec(products, products.size(), 10);

            //This only shows the first box of the order
            String[] box = new String[Boxes.get(0).size()];//get the  first box

            for (int i = 0; i < Boxes.get(0).size(); i++) {
                box[i] = Boxes.get(0).get(i).getLocation();
            }

            String[] route = TSP.getRoute(box);
            StatusPanel.displayRoute(route);
            SchapPanel.drawRoute(route);


           // orderItems = database.getOrderlines(OrderID);
            for (int i = 0; i < Boxes.size(); i++) {
                JLabel doos = new JLabel("Doos " + (i+1) + ": ");
                doos.setFont(new Font("Arial", Font.BOLD, 16));
                if(i>0){
                    doos.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
                }
                products1.add(doos);
                orderItemsPanel.add(doos);
                for (int j = 0; j < Boxes.get(i).size(); j++) {
                    String location = Boxes.get(i).get(j).getLocation();
                    ResultSet stockitem = database.getStockitem(location);
                    JLabel product = new JLabel(location + ": " + stockitem.getInt("StockItemID") + ". " + stockitem.getString("StockItemName") + ". " + stockitem.getString("Weight") + "." );
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
        this.updateUI();
    }

    public void changeOrderColor() throws SQLException {
        orderItemsPanel.removeAll();
        int maxCount = counter1; // Number of times to make labels green
        counter = 0; // Reset counter to start from zero for each "Done"
        for (JLabel label : products1){
            if (counter < maxCount) {
                if (!label.getText().contains("Doos")) {
//                    if (counter == 0){
//                        System.out.println("cHECK");
//                    }

                    label.setForeground(Color.GREEN);
                    counter++;
//                    System.out.println("cHECK");

                    //database.updatepicked(orderlinesIdSorted.get(counter+counter1));
                }


            }
            orderItemsPanel.add(label);
        }
        database.updatepicked(orderlinesIdSorted.get(counter1-1));
        database.updateOrderlineAfterOrder(orderlinesIdSorted.get(counter1-1));
        updateUI();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==jbStartOrder){

                JOptionPane.showMessageDialog(this,"De order is afgerond");

//                    try {
//                        serialCommunicator.sendMessageToArduino("Start");
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
        }

    }


    @Override
    public void onMessageReceived(String message) throws SQLException {
        if (message.equals("Out")){
            System.out.println("out");
            counter1++;
            changeOrderColor();
            System.out.println(counter1);
        }
        if (message.equals("Complete")){
            System.out.println("complete");
            System.out.println(counter1);
            database.updatePickedOrder(order);
            JOptionPane.showMessageDialog(this,"De order is afgerond");
        }
    }
}
