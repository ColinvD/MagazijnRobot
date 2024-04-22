import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderDialog extends JDialog implements ActionListener {
    private JButton jbCancel, jbConfirm;
    private JLabel jlChooseOrder, jlSearchOrder;
    private JTextField jtSearchOrder;
    private JScrollPane jsOrders;
    private JPanel jpHELP, jpA, jpOrders;
    public OrderDialog(){
        setSize(new Dimension(700,500));
        setTitle("Order inladen");
        setModal(true);
        this.setLayout(new GridLayout(2, 1));

        jbCancel = new JButton("Annuleer");
        jbCancel.addActionListener(this);

        jbConfirm = new JButton("Bevestigen");
        jbConfirm.addActionListener(this);

        jlChooseOrder = new JLabel("Kies order om in te laden");
        jlSearchOrder = new JLabel("Zoek ordernummer:");

        jtSearchOrder = new JTextField(15);

        jpOrders = new JPanel();
        jpOrders.setLayout(new GridLayout(500, 3)); //maak row count het aantal orders

        jpHELP = new JPanel();
        jpHELP.setLayout(new BorderLayout());

        jpA = new JPanel();
        jpA.setLayout(new BorderLayout());

        for (int i = 0; i < 500; i++) { //test data
            jpOrders.add(new JLabel("Order " + (i+1)));
            jpOrders.add(new JLabel((int)(Math.random()*31) + "-" + (int)(Math.random()*12) + "-" + ((int)(Math.random()*24) + 2000)));
            jpOrders.add(new JLabel((int)(Math.random()*31) + "-" + (int)(Math.random()*12) + "-" + ((int)(Math.random()*24) + 2000)));
        }

        jsOrders = new JScrollPane(jpOrders);
        jsOrders.setPreferredSize(new Dimension(650, 400));

        add(jpHELP); //????
        add(jpA);
        add(jsOrders);
        jpA.add(jbCancel);
        jpA.add(jbConfirm);
        jpA.add(jlChooseOrder);
        jpA.add(jlSearchOrder);
        jpA.add(jtSearchOrder);
        jpA.add(jsOrders);

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
    }
}
