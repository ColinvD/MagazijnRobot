import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonPanel extends JPanel implements ActionListener {
    private JButton jbOrder;
    private OrderPanel orderPanel;
    public ButtonPanel(OrderPanel orderPanel){
        setPreferredSize(new Dimension(200,400));
        setLayout(new FlowLayout());
        setBackground(Color.white);

        this.orderPanel = orderPanel;

        jbOrder = new JButton("Order inladen");
        jbOrder.addActionListener(this);
        add(jbOrder);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jbOrder){
            try {
                OrderDialog dialog = new OrderDialog(orderPanel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
