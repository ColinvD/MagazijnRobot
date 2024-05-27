import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestSendOrder implements Listener {
    private ArrayList<String> stockLocations = new ArrayList<String>();
    private SerialCommunicator serialCommunicator;
    private int i = 1;
    public TestSendOrder() {
        serialCommunicator = new SerialCommunicator("COM6", 9600);
        serialCommunicator.AddListener(this);
    }

    public void sendOrder(ArrayList<String> values) throws IOException {
        stockLocations = values;
        System.out.println("L" + stockLocations.getFirst() + "1");
        serialCommunicator.sendMessageToArduino("L" + stockLocations.getFirst() + "1");
    }

    @Override
    public void onMessageReceived(String message) throws SQLException, IOException {
        if(message.equals("complete") && i < stockLocations.size()) {
            System.out.println("L" + stockLocations.get(i) + (i + 1));
            serialCommunicator.sendMessageToArduino("L" + stockLocations.get(i) + (i + 1));
            i++;
        }
    }
}
