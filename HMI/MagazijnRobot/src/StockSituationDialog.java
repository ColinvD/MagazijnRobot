import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public StockSituationDialog() {
        setModal(true);
        setTitle("Voorraadsituatie");
        setSize(new Dimension(500, 500));

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
                JButton jButton = new JButton(letter + j);
                jButton.addActionListener(this);
                gridPanel.add(jButton);
                gridButton.add(jButton);
            }
        }

//        value
        valueTitle = new JLabel("Welke waarde wil je meegeven?");

        valuePanel = new JPanel();
        valuePanel.setLayout(new GridLayout(2, 2));
        valuePanel.setPreferredSize(new Dimension( 350, 80));
        valuePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        jcEmpty = new JCheckBox("Leeg");
        jcFull = new JCheckBox("Gevuld met item:");


        String boxValue[] = {"prod1", "tew2", "efa3"};
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

        }
        if (e.getSource() == cancelButton || e.getSource() == confirmButton) {
            setVisible(false);
            return;
        }
        for (JButton jButton : gridButton) {
            if (e.getSource() == jButton) {
                System.out.println(jButton.getText());
                return;
            }
        }
    }
}
