import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CheckPackingslipDialog extends JDialog {
    private JButton cancel,generatePickingslip;

    public CheckPackingslipDialog(ArrayList<JLabel> jLabels) {
        this.setLayout(new GridBagLayout());
        setSize(new Dimension(400,350));
        setTitle("Pakbon genereren,Bekijk Doos");
        setLayout(new FlowLayout(FlowLayout.CENTER,40,5));
        generatePickingslip = new JButton("Genereer Pakbon");
        generatePickingslip.setPreferredSize(new Dimension(150,25));
        cancel = new JButton("Annuleer");
        cancel.setPreferredSize(new Dimension(150,25));
        add(generatePickingslip);
        add(cancel);
        for (JLabel jLabel : jLabels){
            add(jLabel);
        }

        setModal(true);
        setVisible(true);
    }


}
