import java.io.IOException;
import java.sql.SQLException;

public interface Listener {
    void onMessageReceived(String message) throws SQLException, IOException;
}
