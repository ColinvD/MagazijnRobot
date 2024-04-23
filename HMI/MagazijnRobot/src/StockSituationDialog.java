import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

public class StockSituationDialog extends JDialog implements ActionListener {


    private JLabel gridTitle, valueTitle;
    private JButton confirm, cancel;
    private JPanel buttonPanel, gridPanel;
    private ArrayList<JButton> gridButton = new ArrayList<>();

    public StockSituationDialog() {
        setModal(true);
        setTitle("Voorraadsituatie");
        setSize(new Dimension(500, 500));

//        top buttons
        buttonPanel = new JPanel();

        confirm = new JButton("Pas voorraadsituatie aan");
        cancel = new JButton("Annuleer");

        confirm.addActionListener(this);
        cancel.addActionListener(this);

        confirm.setPreferredSize(new Dimension(200, 30));
        cancel.setPreferredSize(new Dimension(200, 30));

        buttonPanel.add(confirm);
        buttonPanel.add(cancel);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

//        grid
        gridPanel = new JPanel();
        gridPanel.setPreferredSize(new Dimension(250, 250));
        gridPanel.setLayout(new GridLayout(5, 5));
        gridTitle = new JLabel("welke waarde wil je meegeven");

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

        add(buttonPanel);
        gridTitle.setPreferredSize(new Dimension(250, 30));
        add(gridTitle);
        add(gridPanel);
        setLayout(new FlowLayout());
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
