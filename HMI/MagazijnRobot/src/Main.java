import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        HMIScreen hmiScreen = new HMIScreen();
        Database database = new Database();
        database.databaseConnect();
//        ResultSet result = database.select("Select * FROM stockitems");
//        database.printResult(result);
        ResultSet order = database.getOrder(30);
        database.printResult(order);
    }
}
