import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

public class StockSituationDialog extends JDialog implements ActionListener {


    private JLabel gridTitle, valueTitle;
    private JButton confirmButton, cancelButton;
    private JPanel buttonPanel, gridPanel, valuePanel;
    private JCheckBox jcEmpty, jcFull;
    private JComboBox jcbProduct;
    private ArrayList<JButton> gridButton = new ArrayList<>();
    private ArrayList<ArrayList<String>> stockStatus = new ArrayList<>();
    private String selectedStockLocation;
    private JButton activeJButton;

    public StockSituationDialog() {
        setModal(true);
        setTitle("Voorraadsituatie");
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        stockStatus.add(new ArrayList<>());
        stockStatus.add(new ArrayList<>());

//        top buttons
        buttonPanel = new JPanel();

        confirmButton = new JButton("Pas voorraadsituatie aan");
        cancelButton = new JButton("Annuleer");

        confirmButton.addActionListener(this);
        cancelButton.addActionListener(this);

        confirmButton.setPreferredSize(new Dimension(200, 30));
        cancelButton.setPreferredSize(new Dimension(200, 30));

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

//        grid
        gridPanel = new JPanel();
        gridPanel.setPreferredSize(new Dimension(250, 250));
        gridPanel.setLayout(new GridLayout(5, 5));
        gridTitle = new JLabel("Welke Positie wil je aanpassen?");

        Database database = new Database();
        database.databaseConnect();
        ArrayList<String> stockList = new ArrayList<>();
        try {
            ResultSet result = database.select("SELECT StockLocation From stockitems");
            while (result.next()) {
                stockList.add(result.getString(1));
            }
            result.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 1; i <= 5; i++) {
            String letter = "";
            switch (i) {
                case 1:
                    letter = "A";
                    break;
                case 2:
                    letter = "B";
                    break;
                case 3:
                    letter = "C";
                    break;
                case 4:
                    letter = "D";
                    break;
                case 5:
                    letter = "E";
                    break;

            }

            for (int j = 1; j <= 5; j++) {
                String StockLocation = letter + j;
                JButton jButton = new JButton(StockLocation);
                stockStatus.get(0).add(StockLocation);
                if (stockList.contains(StockLocation)) {
                    stockStatus.get(1).add("true");
                    jButton.setBackground(Color.lightGray);
                } else {
                    stockStatus.get(1).add("false");
                    jButton.setBackground(Color.white);
                }
                jButton.addActionListener(this);
                gridPanel.add(jButton);
                gridButton.add(jButton);
            }
        }


//        value
        valueTitle = new JLabel("Welke waarde wil je meegeven?");

        valuePanel = new JPanel();
        valuePanel.setLayout(new GridLayout(2, 2));
        valuePanel.setPreferredSize(new Dimension(350, 80));
        valuePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        jcEmpty = new JCheckBox("Leeg");
        jcFull = new JCheckBox("Gevuld met item:");
        jcEmpty.addActionListener(this);
        jcFull.addActionListener(this);

        String boxValue[] = new String[0];
        int count = 1;
        try {
            ResultSet resultCount = database.select("Select count(*) FROM stockitems");
            resultCount.next();
            boxValue = new String[resultCount.getInt("count(*)") + 1];
            boxValue[0] = "";
            ResultSet result = database.select("Select StockItemName FROM stockitems");
            while (result.next()) {
                boxValue[count] = result.getString(1);
                count++;
            }
            result.close();
            database.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        jcbProduct = new JComboBox(boxValue);

        valuePanel.add(jcFull);
        valuePanel.add(jcbProduct);
        valuePanel.add(jcEmpty);


//        fill dialog

        add(buttonPanel);
        gridTitle.setPreferredSize(new Dimension(250, 30));
        add(gridTitle);
        add(gridPanel);
        valueTitle.setPreferredSize(new Dimension(250, 30));
        add(valueTitle);
        add(valuePanel);
        setLayout(new FlowLayout());
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmButton) {
            changeProductStock();
            return;
        }
        if (e.getSource() == cancelButton) {
            setVisible(false);
            return;
        }
        if (e.getSource() == jcEmpty) {
            jcFull.setSelected(false);
        }
        if (e.getSource() == jcFull) {
            jcEmpty.setSelected(false);
        }
        for (JButton jButton : gridButton) {
            if (e.getSource() == jButton) {
                if (selectedStockLocation != null) {
                    if (stockStatus.get(1).get(stockStatus.get(0).indexOf(selectedStockLocation)).equals("true")) {
                        activeJButton.setBackground(Color.lightGray);
                    } else if ((stockStatus.get(1).get(stockStatus.get(0).indexOf(selectedStockLocation)).equals("false"))) {
                        activeJButton.setBackground(Color.white);
                    }
                }

                jButton.setBackground(Color.RED);
                selectedStockLocation = jButton.getText();
                String boxValueSelected = getProductFromStock(jButton);
                activeJButton = jButton;
                if (boxValueSelected == null || boxValueSelected.isEmpty()) {
                    jcEmpty.setSelected(true);
                    jcFull.setSelected(false);
                } else {
                    jcFull.setSelected(true);
                    jcEmpty.setSelected(false);
                }
                jcbProduct.setSelectedItem(boxValueSelected);
                return;
            }
        }
    }

    private void changeProductStock() {
        try {
            Database database = new Database();
            database.databaseConnect();
            int resultUpdate = 0;
            String resultString = "";
            boolean newProduct = false;
            if (jcFull.isSelected() && !jcEmpty.isSelected()) {
                database.update("UPDATE stockitems SET StockLocation = NULL WHERE StockLocation = ?", selectedStockLocation);
                if (!jcbProduct.getSelectedItem().toString().isEmpty()) {
                    resultString = database.selectFirst("SELECT StockLocation FROM stockitems WHERE StockItemName = ?", jcbProduct.getSelectedItem().toString());
                    if (resultString != null && !resultString.isEmpty()) {
                        for (JButton jButton : gridButton) {
                            if (jButton.getText().equals(resultString)) {
                                stockStatus.get(1).set(stockStatus.get(0).indexOf(resultString), "false");
                                jButton.setBackground(Color.WHITE);
                            }
                        }
                    }

                    resultUpdate = database.update("UPDATE stockitems SET StockLocation = ? WHERE StockItemName = ?", selectedStockLocation, jcbProduct.getSelectedItem().toString());
                    newProduct = true;
                }
            } else if (jcEmpty.isSelected() && !jcFull.isSelected()) {
                database.update("UPDATE stockitems SET StockLocation = NULL WHERE StockLocation = ?", selectedStockLocation);
            } else {
                JOptionPane.showMessageDialog(null, "Er zijn geen checkboxes geselecteerd", "error", JOptionPane.ERROR_MESSAGE);
            }
            if (newProduct) {
                if (resultUpdate > 0) {
                    stockStatus.get(1).set(stockStatus.get(0).indexOf(selectedStockLocation), "true");
                    if (resultString == null || resultString.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Het product: " + jcbProduct.getSelectedItem().toString() + " is verplaatst  naar schap: " + selectedStockLocation + ".", "voorraad locatie aangepast", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Het product: " + jcbProduct.getSelectedItem().toString() + " is verplaatst van schap: " + resultString + " naar schap: " + selectedStockLocation + ".", "voorraad locatie aangepast", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Er is iets fout gegaan", "error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (jcEmpty.isSelected() || jcFull.isSelected()){
                stockStatus.get(1).set(stockStatus.get(0).indexOf(selectedStockLocation), "false");
                JOptionPane.showMessageDialog(null, "Het schap: " + selectedStockLocation + " is geleegd.", "voorraad geleegd", JOptionPane.INFORMATION_MESSAGE);
            }
            database.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    private String getProductFromStock(JButton stockLocation) {
        try {
            Database database = new Database();
            database.databaseConnect();

            ResultSet result = database.select("Select stockItemName From StockItems WHERE StockLocation = ?", stockLocation.getText());
            String boxValueSelected = "";
            if(result.next()) {
                if (result.getString(1) != null) {
                    boxValueSelected = result.getString(1);
                }
            }
            result.close();
            database.close();
            return boxValueSelected;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
