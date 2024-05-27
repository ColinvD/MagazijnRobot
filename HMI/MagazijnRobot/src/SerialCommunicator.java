import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialCommunicator implements SerialPortDataListener{
    private final ArrayList<Listener> listeners;
    private String messages;
    private final String portName;
    private final int rate;
    private final SerialPort port;

    public SerialCommunicator(String portName, int bitRate) {
        this.portName = portName;
        this.rate = bitRate;
        this.listeners = new ArrayList<>();

        port = SerialPort.getCommPort(this.portName);
        port.setComPortParameters(rate, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        var hasOpened = port.openPort();
        if(!hasOpened){
            throw new IllegalStateException("Failed to open serial port");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {port.closePort(); }));
        port.addDataListener(this);
    }

    public void sendMessageToArduino(String message) throws IOException{
        message += "\n";
        port.getOutputStream().write(message.getBytes());
        port.getOutputStream().flush();
    }

    @Override
    public int getListeningEvents(){
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent){
        if(serialPortEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED){
            String theMessage = "";
            messages += new String(serialPortEvent.getReceivedData());
            while (messages.contains("\n")) {
                String[] message = messages.split("\\n", 2);
                messages = (message.length > 1) ? message[1] : "";
                theMessage = message[0];
                System.out.println("Message: " + message[0]);
            }
            theMessage = theMessage.trim();
            theMessage = theMessage.replaceFirst("null", "");

            System.out.println(theMessage);
            try {
                NotifyListeners(theMessage);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void AddListener(Listener listener){
        listeners.add(listener);
    }
    public void RemoveListener(Listener listener){
        listeners.remove(listener);
    }
    private void NotifyListeners(String message) throws SQLException, IOException {
        System.out.println("yo: " + message);
        for (Listener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }
}
