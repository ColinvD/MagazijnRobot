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
        int itemCount = database.getOrderSize(OrderID);

        if(itemCount==0){
            orderItemsPanel.add(new JLabel("Lege order."));
        } else {
            ArrayList<Locatie> products = new ArrayList<>();
            while (orderItems.next()){
                Locatie locatie = new Locatie(orderItems.getString("StockLocation"), orderItems.getInt("Weight"));
                for(int i = 0; i < orderItems.getInt("Quantity"); i++){
                    products.add(locatie);
                }
            }
            ArrayList<ArrayList<Locatie>> Boxes = BBP.firstFitDec(products, products.size(), 20);
            for (int i = 0; i < Boxes.size(); i++) {
                String[] Box = new String[Boxes.get(i).size()];
                for (int j = 0; j < Boxes.get(i).size(); j++) {
                    Box[j] = Boxes.get(i).get(j).getLocation();
                }
                System.out.println(Arrays.toString(TSP.getRoute(Box)));
            }

//            while (orderItems.next()) {
//                for (int i = 0; i < orderItems.getInt("Quantity"); i++) {
//                    JLabel product = new JLabel(orderItems.getInt("StockItemID") + ". " + orderItems.getString("StockItemName"));
//                    product.setPreferredSize(new Dimension(280, 12));
//                    orderItemsPanel.add(product);
//                }
//            }
        }
        jlSelectedOrder.setText("Order: " + OrderID + " ");
        this.updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
