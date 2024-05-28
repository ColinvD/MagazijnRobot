import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    //private int orderAmount; //test orders
    //private int selectedOrder = -1;
    private ArrayList<ArrayList> listOrder;
    //private ArrayList<ArrayList> tempOrder;
    //private DateFormat dmy = new SimpleDateFormat("dd/MM/yyyy");
    //private ArrayList<Integer> orderLines = new ArrayList<>();
    private ArrayList<JSpinner> orderlinesSpinners = new ArrayList<>();
    private ArrayList<JButton> deleteButtons = new ArrayList<>();
    private JButton jbCancel, jbConfirm, jbAdd;
    //private JLabel jlChooseOrder;
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
        //this.tempOrder = new ArrayList<>();

        // Haal de ResultSetMetaData op om dynamisch kolomnamen en types te krijgen
        ResultSetMetaData metaData = order.getMetaData();
        int columnCount = metaData.getColumnCount();
        while(order.next()){
            ArrayList<DatabaseValue> row = new ArrayList<>();
            //ArrayList<DatabaseValue> temprow = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = order.getObject(i);

                DatabaseValue newValue = new DatabaseValue(columnName, columnValue);
                //DatabaseValue tempValue = new DatabaseValue(columnName, columnValue);
                row.add(newValue);
                //temprow.add(tempValue);
            }

            listOrder.add(row);
            //tempOrder.add(temprow);
        }

//        ((DatabaseValue)tempOrder.get(0).get(3)).setValue(4);
//        System.out.println((((DatabaseValue)listOrder.get(0).get(3)).getValue()));
//        System.out.println((((DatabaseValue)tempOrder.get(0).get(3)).getValue()));

        jbCancel = new JButton("Annuleer");
        jbCancel.addActionListener(this);

        jbConfirm = new JButton("Bevestigen");
        jbConfirm.addActionListener(this);

        jbAdd = new JButton("Product toevoegen");
        jbAdd.addActionListener(this);

        JLabel jlChooseOrder = new JLabel("Order: " + getDatabaseValue(0,"OrderID").getValue(), SwingConstants.CENTER);
        jlChooseOrder.setPreferredSize(new Dimension(700,20));

        jpOrderlines = new JPanel();
        jpOrderlines.setLayout(new GridBagLayout());

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
        jpTop.add(jbAdd);
        refreshData();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbCancel){
            closeDatabase();
            dispose();
        }
        if (e.getSource() == jbConfirm){
            try{
                ConfirmChange();
                orderPanel.setOrder(orderPanel.getSelectedOrderID());
            } catch (SQLException ex){
                System.out.println("Error occured");
                System.out.println(ex.getMessage());
            }
            closeDatabase();
            dispose();
//            if(selectedOrder != -1){ //-1 = geen order selected
//                try {
//                    orderPanel.setOrder(selectedOrder);
//                    System.out.println("Order inladen gelukt.");
//                } catch (SQLException ex) {
//                    System.out.println("Order inladen mislukt.");
//                    throw new RuntimeException(ex);
//                }
//            }
//            closeDatabase();
//            dispose();
        }
        if(e.getSource() == jbAdd){
            AddProductDialog addDialog = new AddProductDialog(orderPanel.getSelectedOrderID());
            if(addDialog.isAdd()){
                //DatabaseValue newProduct = new DatabaseValue("StockItemID", addDialog.getChosenProductID());
                //ArrayList<DatabaseValue> temp = new ArrayList<>();
                //temp.add(newProduct);

                if(addDialog.getWantedQuantity()>0){
                    ArrayList<DatabaseValue> row = makeFakeOrderLine(addDialog.getChosenProductID(), addDialog.getWantedQuantity());
                    listOrder.add(row);
                    refreshData();
                    //jsOrders.updateUI();
                }
            }
        }
        for(int i = 0; i < deleteButtons.size(); i++){
            if(e.getSource() == deleteButtons.get(i)){
                listOrder.remove(i);
                orderlinesSpinners.remove(i);
                deleteButtons.remove(i);
                refreshData();
            }
        }
        /*if(e.getSource() == jbSearch){
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
        jpOrderlines.removeAll();

        //orderLines.clear();
        orderlinesSpinners.clear();
        deleteButtons.clear();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
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
//            for (int j = 0; j < orderline.size(); j++) {
//                if(orderline.get(j).getColomn().equals("StockItemID")){
//                    orderLines.add((int)orderline.get(j).getValue());
//                    break;
//                }
//            }
            //orderLines.add((int) orderline.get(2).getValue());
            //orderLines.add((int) getDatabaseValue(i, "OrderID").getValue());
            int productID = (int) getDatabaseValue(i, "StockItemID").getValue();
            String itemName = (String) getDatabaseValue(i, "Description").getValue();
            int itemQuantity = (int) getDatabaseValue(i, "Quantity").getValue();
            c.gridx = 0;
            c.gridy = i+1;
            jpOrderlines.add(new JLabel("" + productID, SwingConstants.CENTER), c);
            c.gridx = 1;
            jpOrderlines.add(new JLabel(itemName, SwingConstants.CENTER), c);

            SpinnerNumberModel model = new SpinnerNumberModel(itemQuantity, 1, null, 1);
            JSpinner quantity = new JSpinner(model);
            quantity.setPreferredSize(new Dimension(50,20));
            quantity.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    OnSpinnerChange((int)quantity.getValue(), quantity);
                }
            });
            c.gridx = 2;
            orderlinesSpinners.add(quantity);
            jpOrderlines.add(quantity, c);
            JButton deleteButton = new JButton("X");
            deleteButton.addActionListener(this);
            deleteButton.setBackground(Color.red);
            deleteButton.setForeground(Color.white);
            deleteButtons.add(deleteButton);
            c.gridx = 3;
            jpOrderlines.add(deleteButton,c);
        }

        if(listOrder.isEmpty()) {
            jpOrderlines.removeAll();
            jpOrderlines.setLayout(new GridBagLayout());
            jpOrderlines.add(new JLabel("Geen orders gevonden.", SwingConstants.CENTER));
        }

        jpOrderlines.revalidate();
        jsOrders.updateUI();
    }

    public void closeDatabase(){
        try{
            database.close();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    private void OnSpinnerChange(int value, JSpinner spinner){
        DatabaseValue oldValue = getDatabaseValue(orderlinesSpinners.indexOf(spinner),"Quantity");
        System.out.println("Hi i'm the spinner from: " + orderlinesSpinners.indexOf(spinner) + ", with the new Value: " + value);
        oldValue.setValue(value);
    }

    private void ConfirmChange() throws SQLException {
        ResultSet currentOrder = database.getOrderlines(orderPanel.getSelectedOrderID());
        while (currentOrder.next()){
            int dbOrderlineID = currentOrder.getInt("OrderLineID");
            if(dbOrderlineID == (int)getDatabaseValue(0, "OrderLineID").getValue()){
                // Update uitvoeren
                System.out.println("Updating orderline");
                database.update("UPDATE orderlines SET Quantity= ? WHERE OrderLineID = ?", String.valueOf(getDatabaseValue(0, "Quantity").getValue()), String.valueOf(dbOrderlineID));
                // Haal orderregel uit de array zodat de toevoegingen overblijven
                listOrder.removeFirst();
            }
            else{
                // item is verwijderd
                System.out.println("Item wordt verwijderd");
                database.deleteOrderLine(currentOrder.getInt("OrderLineID"));
            }
        }
        if(!listOrder.isEmpty()){
            // add overige dingen aan database
            System.out.println("Item wordt toegevoegd");
            for (ArrayList<DatabaseValue> arrayList : listOrder) {
                database.insertOrderLines(arrayList);
            }
//            if(pickedID != 0 && amount >0){
//                try{
//                    database.insertOrderLines(orderID, amount, database.select("Select StockItemID, StockItemName, UnitPackageID, TaxRate, UnitPrice From stockitems Where StockItemID = ?", "" + productIDs.get(pickedID-1)));
//                } catch (SQLException ex){
//                    throw new RuntimeException(ex.getMessage());
//                    //System.out.println(ex.getMessage());
//                }
//            }
        }
    }

    private DatabaseValue getDatabaseValue(int lineRow, String searchItem){
        ArrayList<DatabaseValue> orderline = listOrder.get(lineRow);
        for (DatabaseValue databaseValue : orderline) {
            if(databaseValue.getColomn().equals(searchItem)){
                return databaseValue;
            }
//            switch (databaseValue.getColomn()) {
//                case "OrderID", "Quantity", "Description", "StockItemID":
//                    return databaseValue.getValue();
//                default:
//                    break;
//            }
        }
        return null;
    }

    private ArrayList<DatabaseValue> makeFakeOrderLine(int productID, int wantedQuantity) {
        ArrayList<DatabaseValue> productLine = new ArrayList<>();
        System.out.println(productID);
        try{
            ResultSet productData = database.select("Select StockItemID, StockItemName, UnitPackageID, TaxRate, UnitPrice From stockitems Where StockItemID = ?", ""+productID);
            productData.next();
            productLine.add(new DatabaseValue("OrderLineID",-1));
            productLine.add(new DatabaseValue("OrderID", orderPanel.getSelectedOrderID()));
            productLine.add(new DatabaseValue("Description",productData.getString("StockItemName")));
            productLine.add(new DatabaseValue("Quantity",wantedQuantity));
            ResultSetMetaData data = productData.getMetaData();
            for(int i = 1; i <= data.getColumnCount(); i++){
                DatabaseValue value  = new DatabaseValue(data.getColumnName(i),productData.getObject(i));
                productLine.add(value);
            }
        } catch (SQLException ex){
            throw  new RuntimeException(ex.getMessage());
        }
        return productLine;
    }
}
