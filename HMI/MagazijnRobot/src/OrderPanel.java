import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderPanel extends JPanel implements ActionListener {
    private JButton jbOrder;
    public OrderPanel(){
        setPreferredSize(new Dimension(200,400));
        setLayout(new FlowLayout());
        setBackground(Color.white);
        jbOrder = new JButton("Order");
        jbOrder.addActionListener(this);
        add(jbOrder);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbOrder){
            OrderDialog dialog = new OrderDialog();
        }
    }
}
