import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderDialog extends JDialog implements ActionListener {
    public OrderDialog(){
        setSize(new Dimension(500,500));
        setTitle("Orders");
        setModal(true);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
