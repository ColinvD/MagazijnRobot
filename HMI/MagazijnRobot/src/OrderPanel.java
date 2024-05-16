import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderPanel extends JPanel implements ActionListener {
    private ResultSet selectedOrder;
    private Database database;
    private JLabel jlSelectedOrder;
    private ResultSet orderItems;
    private JPanel orderItemsPanel;
    private JButton jbStartOrder;
    private JScrollPane orderJSP;
    public OrderPanel(){
        setPreferredSize(new Dimension(350,400));
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
        orderJSP.setPreferredSize(new Dimension(350,360));
        add(jlSelectedOrder);
        add(jbStartOrder);
        add(orderJSP);
    }
    public void setOrder(int OrderID) throws SQLException {
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
                Locatie locatie = new Locatie(orderItems.getString("StockLocation"), orderItems.getInt("Weight"));
                for(int i = 0; i < orderItems.getInt("Quantity"); i++){
                    products.add(locatie);
                }
            }
            ArrayList<ArrayList<Locatie>> Boxes = BBP.firstFitDec(products, products.size(), 20);

            //This only shows the first box of the order
            String[] box = new String[Boxes.get(0).size()];//get the  first box

            for (int i = 0; i < Boxes.get(0).size(); i++) {
                box[i] = Boxes.get(0).get(i).getLocation();
            }

            String[] route = TSP.getRoute(box);
            StatusPanel.displayRoute(route);
            SchapPanel.drawRoute(route);
            //

            orderItems = database.getOrderlines(OrderID);

            for (int i = 0; i < Boxes.size(); i++) {
                JLabel doos = new JLabel("Doos " + (i+1) + ": ");
                doos.setFont(new Font("Arial", Font.BOLD, 16));
                if(i>0){
                    doos.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
                }
                orderItemsPanel.add(doos);
                for (int j = 0; j < Boxes.get(i).size(); j++) {
                    String location = Boxes.get(i).get(j).getLocation();
                    ResultSet stockitem = database.getStockitem(location);
                    JLabel product = new JLabel(location + ": " + stockitem.getInt("StockItemID") + ". " + stockitem.getString("StockItemName") + ".");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==jbStartOrder){

        }
    }
}
