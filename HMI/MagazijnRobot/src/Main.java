import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        SendOrder sendOrder = new SendOrder();
        Thread.sleep(4000);
        ArrayList<String> values = new ArrayList<String>();
        values.add("E5");
        values.add("E4");
        values.add("E3");
        sendOrder.sendOrder(values);


        HMIScreen hmiScreen = new HMIScreen();
        Database d = new Database();
        try {
            d.databaseConnect();
            System.out.println("Database Connected");
        } catch (Exception e) {
            System.out.println("Database Not Connected");
            System.out.println(e.getMessage());
        }
    }
}
