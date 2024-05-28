import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SendOrder implements Listener {
    private ArrayList<String> stockLocations = new ArrayList<String>();
    private SerialCommunicator serialCommunicator;
    private int i;
    public SendOrder(SerialCommunicator serialCommunicator) {
        this.serialCommunicator = serialCommunicator;
        serialCommunicator.AddListener(this);
    }

    public void sendOrderValues(ArrayList<String> values) throws IOException {
        i = 1;
        stockLocations = values;
        System.out.println("L" + stockLocations.getFirst() + "1");
        serialCommunicator.sendMessageToArduino("L" + stockLocations.getFirst() + "1");
    }

    @Override
    public void onMessageReceived(String message) throws SQLException, IOException {
        if(!message.contains("X:") && !message.isEmpty()) {
            System.out.println(message);
        }
        if(message.equals("Out") && i < stockLocations.size()) { //per item die completed is
            System.out.println("L" + stockLocations.get(i) + (i + 1));
            serialCommunicator.sendMessageToArduino("L" + stockLocations.get(i) + (i + 1));
            i++;
        } else if (message.equals("Out") && i==stockLocations.size()) {
            serialCommunicator.sendMessageToArduino("GoToStart");
        }

        if(message.equals("Done") && i==stockLocations.size()){ //als doos completed is
            System.out.println("Box completed");
        }
    }
}
