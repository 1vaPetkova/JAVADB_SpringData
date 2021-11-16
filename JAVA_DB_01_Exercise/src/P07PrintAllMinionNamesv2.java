import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class P07PrintAllMinionNamesv2 {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM minions");
        ResultSet rs = ps.executeQuery();
        Deque<String> queue = new ArrayDeque<>();
        Deque<String> stack = new ArrayDeque<>();
        while (rs.next()) {
            queue.offer(rs.getString("name"));
            stack.push(rs.getString("name"));
        }
        int size = queue.size();
        while (queue.size() > size / 2) {
            System.out.println(queue.poll());
            System.out.println(stack.pop());
        }
        if (size % 2 != 0) {
            System.out.println(stack.pop());
        }
    }

    private static Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");
        return DriverManager.getConnection(CONNECTION_URL + DB_NAME, properties);
    }

}
