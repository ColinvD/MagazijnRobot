import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SchapPanel extends JPanel implements ActionListener, Listener {
    public  JButton In;
    public JButton Out;
    public JLabel label;
    public SerialCommunicator comm;

    public SchapPanel(){
        setPreferredSize(new Dimension(400,400));
        setLayout(new FlowLayout());
        setBackground(Color.red);
        comm = new SerialCommunicator("COM3", 9600);
        comm.AddListener(this);
        In = new JButton("In");
        In.addActionListener(this);
        Out = new JButton("Uit");
        Out.addActionListener(this);
        label = new JLabel("");
        add(In);
        add(Out);
        add(label);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == In){
            try {
                comm.sendMessageToArduino("In");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == Out){
            try {
                comm.sendMessageToArduino("Uit");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void onMessageReceived(String message) {
        if(message.equals("Done")){
            label.setText("Done!!!!!!!!!");
        }
    }
}
