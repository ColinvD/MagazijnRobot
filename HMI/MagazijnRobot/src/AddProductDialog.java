import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddProductDialog extends JDialog implements ActionListener {

    private JButton jbCancel, jbAdd;
    private JComboBox jcbProducts;
    private JSpinner jsAmount;
    private Database database;
    private ArrayList<Integer> productIDs;
    private int orderID;
    private int chosenProductID = -1;
    private boolean add = false;

    public AddProductDialog(int orderID) {
        setSize(new Dimension(700,350));
        setTitle("Product Toevoegen");
        setLayout(new FlowLayout());
        setModal(true);

        this.orderID = orderID;
        database = new Database();
        database.databaseConnect();

        jbAdd = new JButton("Bevestig");
        jbAdd.addActionListener(this);
        jbCancel = new JButton("Annuleer");
        jbCancel.addActionListener(this);

        String boxValue[] = new String[0];
        productIDs = new ArrayList<>();
        int count = 1;
        try {
            ResultSet resultCount = database.select("Select count(*) FROM stockitems");
            resultCount.next();
            boxValue = new String[resultCount.getInt("count(*)") + 1];
            boxValue[0] = "";
            ResultSet result = database.select("Select StockItemName, StockItemID FROM stockitems");
            while (result.next()) {
                boxValue[count] = result.getString(1);
                productIDs.add(result.getInt(2));
                count++;
            }
            result.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        jcbProducts = new JComboBox(boxValue);

        SpinnerNumberModel model = new SpinnerNumberModel(0, 1, null, 1);
        jsAmount = new JSpinner(model);
        jsAmount.setPreferredSize(new Dimension(50,20));

        add(jbAdd);
        add(jbCancel);
        add(new JLabel("Welk product wil je toevoegen?"));
        add(jcbProducts);
        add(new JLabel("Hoeveel?"));
        add(jsAmount);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbAdd){
            int pickedID = jcbProducts.getSelectedIndex();
            add = true;
            chosenProductID = productIDs.get(pickedID-1);
//            System.out.println(pickedID);
//            int amount = (int)jsAmount.getValue();
//            if(pickedID != 0 && amount >0){
//                try{
//                    database.insertOrderLines(orderID, amount, database.select("Select StockItemID, StockItemName, UnitPackageID, TaxRate, UnitPrice From stockitems Where StockItemID = ?", "" + productIDs.get(pickedID-1)));
//                } catch (SQLException ex){
//                    throw new RuntimeException(ex.getMessage());
//                    //System.out.println(ex.getMessage());
//                }
//            }
        }
        dispose();
    }

    public int getChosenProductID() {
        return chosenProductID;
    }

    public boolean isAdd() {
        return add;
    }

    public int getWantedQuantity() {
        return (int) jsAmount.getValue();
    }
}
