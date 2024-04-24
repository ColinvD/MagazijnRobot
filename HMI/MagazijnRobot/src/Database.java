import java.sql.*;

public class Database {

    private String url = "jdbc:mysql://localhost/";
    private String dbName = "magazijnrobot";
    private String userName = "root";
    private String dbpassword = "";

    private Statement statement;

    //function to connect to the xampp server
    public void databaseConnect() {
        try {
            Connection connection = DriverManager.getConnection(url + dbName, userName, dbpassword);
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

    public void printResult(ResultSet rs) throws SQLException {
        while (rs.next()) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                System.out.print(rs.getMetaData().getColumnLabel(i) + ": ");
                System.out.print(rs.getString(i) + "; ");
            }
            System.out.println("");
        }
    }

    public ResultSet getOrder(int OrderID) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE OrderID = " + OrderID + ";");
        return rs;
    }

    public ResultSet getOrderlines(int OrderID) throws  SQLException{
        ResultSet rs = statement.executeQuery("SELECT * FROM orderlines o INNER JOIN stockitems s ON s.StockItemID = o.StockItemID WHERE OrderID = " + OrderID + ";");
        return rs;
    }
}
