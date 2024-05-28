import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
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
