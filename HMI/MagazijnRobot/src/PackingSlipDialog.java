import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackingSlipDialog extends JDialog implements ActionListener {
    private String selectedBox;
    ArrayList<JLabel> allBoxesInfo;
    ArrayList<JButton> allBoxes = new ArrayList<>();
    private int boxAmount;
    private OrderPanel orderPanel;
    private JScrollPane boxes;
    private JLabel textGeneratePackingSlip,chosenBox;

    private JButton confirm,cancel;
    private Database database;
    private JPanel top,bottom;

    public PackingSlipDialog(OrderPanel orderPanel) throws SQLException {
        this.orderPanel = orderPanel;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        setSize(new Dimension(500,350));
        setTitle("Pakbon genereren");
        setModal(true);
        boxes = new JScrollPane();
        boxes.setPreferredSize(new Dimension(500,850));
        boxes.getVerticalScrollBar().setUnitIncrement(17);
        System.out.println(javax.swing.UIManager.getDefaults().getFont("Label.font"));
        textGeneratePackingSlip = new JLabel("Selecteer de doos waar u de pakbon voor wil genereren",SwingConstants.CENTER);
        textGeneratePackingSlip.setFont(new Font("Dialog",Font.BOLD,15));

        cancel = new JButton("Annuleer");
        cancel.addActionListener(this);

        confirm = new JButton("Bevestigen");
        confirm.addActionListener(this);

        chosenBox = new JLabel("Gekozen doos: geen", SwingConstants.CENTER);
        chosenBox.setPreferredSize(new Dimension(500,20));
        chosenBox.setFont(new Font("Dialog",Font.BOLD,12));
        top = new JPanel(new FlowLayout());
        bottom = new JPanel(new GridLayout(getBoxAmount(),1));
        boxes = new JScrollPane(bottom);


        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        this.add(top, c);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 3;
        top.add(confirm);
        top.add(cancel);
        top.add(chosenBox);
        top.add(textGeneratePackingSlip);
        this.add(boxes,c);
        getboxes();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel){
            closeDatabase();
            dispose();

        }
        if (e.getSource() == confirm){
            ArrayList<JLabel> box = new ArrayList<>();
            boolean stillinbox = false;
            for (JLabel item : allBoxesInfo){
                if (item.getText().contains(selectedBox)){
                   stillinbox = true;
                }
                else if (item.getText().contains("Doos")){
                    stillinbox = false;
                }
                if (stillinbox){
                    box.add(item);
                }
            }
            CheckPackingslipDialog checkPackingslipDialog = new CheckPackingslipDialog(box);
        }
        for (JButton allBox : allBoxes) {
            if (e.getSource() == allBox) {
                System.out.println(allBox.getText());
                selectedBox = allBox.getText();
                chosenBox.setText("Gekozen Doos: " + selectedBox);
                top.updateUI();
            }
        }
    }
    public void closeDatabase(){
        try{
            database.close();
        } catch (Exception exception){

        }
    }
    public int getBoxAmount() {
      return boxAmount;
    }
    public void getboxes(){
        bottom.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        allBoxesInfo = orderPanel.getBoxes();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5,5,0,5);
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;
        for (JLabel jLabel : allBoxesInfo) {
            System.out.println(jLabel.getText());
            if (jLabel.getText().contains("Doos")) {
                jLabel.setText(jLabel.getText().replace(":",""));
                JButton value = new JButton(jLabel.getText());
                value.addActionListener(this);
                bottom.add(value,c);
                allBoxes.add(value);
                c.gridy++;
            }
            boxAmount++;
        }
        bottom.revalidate();
        boxes.updateUI();
    }
}
