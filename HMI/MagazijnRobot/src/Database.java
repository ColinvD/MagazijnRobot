import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    private String url = "jdbc:mysql://localhost/";
    private String dbName = "magazijnrobot";
    private String userName = "root";
    private String dbpassword = "";

    private Statement statement;
    private Connection connection;

    //function to connect to the xampp server
    public void databaseConnect() {
        try {
            connection = DriverManager.getConnection(url + dbName, userName, dbpassword);
            statement = connection.createStatement();

        } catch (Exception e) {
            System.out.println("Database Not Connected");
            System.out.println(e.getMessage());
        }
    }

    public ResultSet select(String query) throws SQLException {
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    public ResultSet select(String query, String value) throws SQLException {
        PreparedStatement s = connection.prepareStatement(query);
        s.setString(1, value);
        ResultSet rs = s.executeQuery();
        return rs;
    }

    public String selectFirst(String query, String value) throws SQLException {
        PreparedStatement s = connection.prepareStatement(query);
        s.setString(1, value);
        ResultSet rs = s.executeQuery();
        rs.next();
        String result = rs.getString(1);
        rs.close();
        return result;
    }

    public int update(String query) throws SQLException {
        return statement.executeUpdate(query);
    }

    public int update(String query, String value) throws SQLException {
        PreparedStatement s = connection.prepareStatement(query);
        s.setString(1, value);
        return s.executeUpdate();
    }

    public int update(String query, String value, String value2) throws SQLException {
        PreparedStatement s = connection.prepareStatement(query);
        s.setString(1, value);
        s.setString(2, value2);
        System.out.println(s);
        return s.executeUpdate();
    }

    public void printResult(ResultSet rs) throws SQLException {
        while (rs.next()) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                System.out.print(rs.getMetaData().getColumnLabel(i) + ": ");
                System.out.print(rs.getString(i) + "; ");
            }
            System.out.println("");
        }
    }

    public void close() throws SQLException {
        statement.close(); //sluit ook de resultset
        connection.close();
    }

    public ArrayList<String> getOrders() throws SQLException {
        Database database = new Database();
        database.databaseConnect();
        ArrayList<String> stockList = new ArrayList<>();
        try {
            ResultSet result = database.select("SELECT StockLocation From stockitems");
            while (result.next()) {
                stockList.add(result.getString(1));
            }
            result.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stockList;
    }

    public ResultSet getOrder(int OrderID) throws SQLException {
        String query = "SELECT * FROM orders WHERE OrderID = ? ;";
        PreparedStatement s = connection.prepareStatement(query);
        s.setInt(1, OrderID);
        ResultSet rs = s.executeQuery();
        return rs;
    }

    public ResultSet getOrderlines(int OrderID) throws SQLException {
        String query = "SELECT * FROM orderlines o INNER JOIN stockitems s ON s.StockItemID = o.StockItemID WHERE OrderID = ? ;";
        PreparedStatement s = connection.prepareStatement(query);
        s.setInt(1, OrderID);
        ResultSet rs = s.executeQuery();
        return rs;
    }

    public int getItemQuantity(int StockitemID) throws SQLException {
        String query = "SELECT * FROM stockitemholdings WHERE StockitemID = ? ;";
        PreparedStatement s = connection.prepareStatement(query);
        s.setInt(1, StockitemID);
        ResultSet rs = s.executeQuery();
        rs.next();
        return rs.getInt("QuantityOnHand");
    }

    //    public int getItemQuantity(String StockitemName) throws SQLException {
//        ResultSet rs = statement.executeQuery("SELECT QuantityOnHand FROM stockitemholdings sih JOIN stockitems si ON sih.StockItemID = si.StockItemID  WHERE si.StockItemName LIKE '%" + StockitemName + "%';");
//        rs.next();
//        int Quantity = -1;
//        try{
//            Quantity = rs.getInt("QuantityOnHand");
//        } catch (Exception e){
//
//        }
//        return Quantity;
//    }
    public static String getProductFromStock(JButton stockLocation) {
        try {
            Database database = new Database();
            database.databaseConnect();

            ResultSet result = database.select("Select stockItemName From StockItems WHERE StockLocation = ?", stockLocation.getText());
            String boxValueSelected = "";
            if (result.next()) {
                if (result.getString(1) != null) {
                    boxValueSelected = result.getString(1);
                }
            }
            result.close();
            database.close();
            return boxValueSelected;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<String> getStockInfo(String positie) throws SQLException {
        ArrayList<String> results = new ArrayList<>();
        positie = "'" + positie + "'";
        ResultSet result = statement.executeQuery("Select StockItemID,StockItemName From stockitems WHERE StockLocation = " + positie);

        if (result.next()) {
            int StockitemId = result.getInt("StockItemID");
            String StockitemName = result.getString("StockItemName");
            int voorraad = getItemQuantity(StockitemId);
            results.add("Naam: " + StockitemName);
            results.add("Voorraad: " + voorraad);
        }
        return results;
    }

    public ArrayList<Locatie> getWeights() throws SQLException {
        Database database = new Database();
        database.databaseConnect();
//        int i = 0;
        ResultSet result1 = database.select("SELECT Count(*) FROM stockitems WHERE StockLocation IS NOT NULL ");
        result1.next();
        ArrayList<Locatie> weights = new ArrayList<>();
        ResultSet result = database.select("SELECT StockLocation,Weight FROM stockitems WHERE StockLocation IS NOT NULL ");
        while (result.next()) {
            int d = result.getInt("Weight");
            weights.add(new Locatie(result.getString("StockLocation"),d));
        }
        return weights;


    }
}



