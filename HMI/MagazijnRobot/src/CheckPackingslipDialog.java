import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CheckPackingslipDialog extends JDialog implements ActionListener {
    ArrayList<ArrayList> dataToSend;
    private JButton cancel, generatePickingslip;
    private OrderPanel orderPanel;
    private JPanel top,bottom;
    private ArrayList<JLabel> items;
    private Database database;

    public CheckPackingslipDialog(ArrayList<JLabel> items, OrderPanel orderPanel) {
        this.items = items;
        this.orderPanel = orderPanel;
        this.setLayout(new GridBagLayout());
        setSize(new Dimension(650, 250));
        setTitle("Pakbon genereren,Bekijk Doos");
        setLayout(new FlowLayout());
        generatePickingslip = new JButton("Genereer Pakbon");
        generatePickingslip.setPreferredSize(new Dimension(150, 25));
        cancel = new JButton("Annuleer");
        cancel.setPreferredSize(new Dimension(150, 25));
        generatePickingslip.addActionListener(this);
        cancel.addActionListener(this);
        top = new JPanel(new FlowLayout(FlowLayout.CENTER,20, 5));
        bottom = new JPanel(new GridLayout(items.size(),1));
        add(generatePickingslip);
        add(cancel);
        for (JLabel jLabel : items) {
            bottom.add(jLabel);
        }
        top.add(cancel,generatePickingslip);
        add(top);
        add(bottom);
        setModal(true);
        this.database = new Database();
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            dispose();
        }
        if (e.getSource() == generatePickingslip) {

            try {
                //myObj.createNewFile();
                FileWriter myWriter = new FileWriter("./HMI/MagazijnRobot/Packing_Slips/" + orderPanel.getSelectedOrderID() + "_" + items.getFirst().getText() + ".txt");
                database.databaseConnect();
                try {
                    dataToSend = database.getPackingSlipData(orderPanel.getSelectedOrderID());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(database.getValueInRow(dataToSend.get(0), "Description").toString());
                boolean printedAddress = false;
                for (ArrayList data : dataToSend) {
                    if (!printedAddress) {
                        myWriter.write("\n" + database.getValueInRow(data, "CustomerName"));
                        myWriter.write("\n" + database.getValueInRow(data, "DeliveryAddressLine1"));
                        myWriter.write("\n" + database.getValueInRow(data, "CityName"));
                        myWriter.write("\n" + database.getValueInRow(data, "PostalAddressLine1"));
                        myWriter.write("\n" + database.getValueInRow(data, "PackageDate"));
                        myWriter.write("\n\nAlle producten:");
                        printedAddress = true;
                    }
                    for (JLabel item : items) {
                        if (item.getText().contains((String) database.getValueInRow(data, "Description"))) {
                            myWriter.write("\nProduct beschrijving: " + database.getValueInRow(data, "Description"));
                            myWriter.write("\nGepakte aantal: " + database.getValueInRow(data, "PickedQuantity"));
                        }
                    }
                }

                myWriter.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


}
