import java.sql.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SQLException {
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
