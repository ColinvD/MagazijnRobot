import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        TestSendOrder testSendOrder = new TestSendOrder();
        Thread.sleep(4000);
        ArrayList<String> values = new ArrayList<String>();
        values.add("E5");
        values.add("B2");
        values.add("E1");
        testSendOrder.sendOrder(values);


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
