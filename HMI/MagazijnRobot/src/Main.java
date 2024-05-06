import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        HMIScreen hmiScreen = new HMIScreen();
        Database d = new Database();
        d.databaseConnect();
        System.out.println("ID 1: " + d.getItemQuantity(1));
    }
}
