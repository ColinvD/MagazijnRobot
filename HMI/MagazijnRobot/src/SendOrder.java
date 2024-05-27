import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SendOrder implements Listener {
    private ArrayList<String> stockLocations = new ArrayList<String>();
    private SerialCommunicator serialCommunicator;
    private int i;
    public SendOrder() {
        serialCommunicator = new SerialCommunicator("COM6", 9600);
        serialCommunicator.AddListener(this);
    }

    public void sendOrder(ArrayList<String> values) throws IOException {
        i = 1;
        stockLocations = values;
        System.out.println("L" + stockLocations.getFirst() + "1");
        serialCommunicator.sendMessageToArduino("L" + stockLocations.getFirst() + "1");
    }

    @Override
    public void onMessageReceived(String message) throws SQLException, IOException {
//        System.out.println(message);
//        System.out.println("i = " + i);
        if(message.equals("complete") && i < stockLocations.size()) { //per item die completed is
            System.out.println("L" + stockLocations.get(i) + (i + 1));
            serialCommunicator.sendMessageToArduino("L" + stockLocations.get(i) + (i + 1));
            i++;
        } else if (message.equals("complete") && i==stockLocations.size()) {
            serialCommunicator.sendMessageToArduino("GoToStart");
        }

        if(message.equals("Finished") && i==stockLocations.size()){ //als doos completed is
            System.out.println("Box completed");
        }
    }
}
