import java.sql.*;

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
            System.out.println("Database Connected");
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

    public ResultSet getOrder(int OrderID) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE OrderID = " + OrderID + ";");
        return rs;
    }

    public ResultSet getOrderlines(int OrderID) throws  SQLException{
        ResultSet rs = statement.executeQuery("SELECT * FROM orderlines o INNER JOIN stockitems s ON s.StockItemID = o.StockItemID WHERE OrderID = " + OrderID + ";");
        return rs;
    }

    public int getItemQuantity(int StockitemID) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM stockitemholdings WHERE StockitemID = " + StockitemID + ";");
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
}
