import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        HMIScreen hmiScreen = new HMIScreen();
        Database database = new Database();
        database.databaseConnect();
        ResultSet result = database.getOrder(35);
        ResultSet order = database.getOrder(39);
        database.printResult(order);
    }
}
