
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/miniproject"; // Replace 'blood_db' with your DB name
        String username = "root"; // Your MySQL username
        String password = "WJ28@krhps"; // password

        try {
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to MySQL database!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
