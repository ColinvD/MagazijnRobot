import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OrderDialog extends JDialog implements ActionListener {
    final private int orderAmount = 25; //test orders
    private int selectedOrder = -1;
    private ArrayList<Integer> foundOrders = new ArrayList<>();
    private ArrayList<JButton> orderButtons = new ArrayList<>();
    private JButton jbCancel, jbConfirm, jbSearch;
    private JLabel jlChooseOrder, jlSearchOrder;
    private JTextField jtSearchOrder;
    private JScrollPane jsOrders;
    private JPanel jpA, jpOrders;
    public OrderDialog(){
        setSize(new Dimension(700,500));
        setTitle("Order inladen");
        setModal(true);
        this.setLayout(new GridLayout(2,1));

        jbCancel = new JButton("Annuleer");
        jbCancel.addActionListener(this);

        jbConfirm = new JButton("Bevestigen");
        jbConfirm.addActionListener(this);

        jbSearch = new JButton("Zoek");
        jbSearch.addActionListener(this);

        jlChooseOrder = new JLabel("Kies order om in te laden");
        jlSearchOrder = new JLabel("Zoek ordernummer:");

        jtSearchOrder = new JTextField(15);
        jtSearchOrder.addActionListener(this);

        jpOrders = new JPanel();
        jpOrders.setLayout(new GridLayout(orderAmount, 3)); //maak row count het aantal orders

        jpA = new JPanel();
        jpA.setLayout(new FlowLayout());
        jsOrders = new JScrollPane(jpOrders);
        jsOrders.setPreferredSize(new Dimension(700,250));

        refreshData();

        this.add(jpA);
        this.add(jsOrders);
        jpA.add(jbCancel);
        jpA.add(jbConfirm);
        jpA.add(jlChooseOrder);
        jpA.add(jlSearchOrder);
        jpA.add(jtSearchOrder);
        jpA.add(jbSearch);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbCancel){
            dispose();
        }
        if (e.getSource() == jbConfirm){
            dispose();
        }
        if(e.getSource() == jbSearch){
            jpOrders.removeAll();
            refreshData();
            jsOrders.updateUI();
        }
        for (int i = 0; i < orderButtons.size(); i++) {
            if(e.getSource()==orderButtons.get(i)){
                selectedOrder = foundOrders.get(i);
                jlChooseOrder.setText("Gekozen order: Order " + (foundOrders.get(i)+1));
                jpA.updateUI();
            }
        }
    }

    public void refreshData(){
        foundOrders.clear();
        orderButtons.clear();
        for (int i = 0; i < orderAmount; i++) {
            String orderNumber = "";
            orderNumber += (i+1);
            if (jtSearchOrder.getText().isEmpty() || orderNumber.contains(jtSearchOrder.getText())) {
                foundOrders.add(i);
          }
        }
        jpOrders.setLayout(new GridLayout(foundOrders.size(),3));
        for (int i = 0; i < foundOrders.size(); i++) {
            JButton orderButton = new JButton("Selecteer ");
            orderButton.addActionListener(this);
            orderButtons.add(orderButton);
            jpOrders.add(orderButton);
            jpOrders.add(new JLabel("Order: " + (foundOrders.get(i)+1)));
            jpOrders.add(new JLabel((int) (Math.random() * 31) + "-" + (int) (Math.random() * 12) + "-" + ((int) (Math.random() * 24) + 2000)));
            jpOrders.add(new JLabel((int) (Math.random() * 31) + "-" + (int) (Math.random() * 12) + "-" + ((int) (Math.random() * 24) + 2000)));
        }
    }
}
