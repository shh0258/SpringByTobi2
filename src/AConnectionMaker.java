import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AConnectionMaker implements ConnectionMaker{
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/Tobi?serverTimezone=UTC", "root", "test123");
        return c;
    }
}
