import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class P07PrintAllMinionNames {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM minions");
        ResultSet rs = ps.executeQuery();
        List<String> names = new ArrayList<>();
        List<String> output = new ArrayList<>();
        while (rs.next()) {
            names.add(rs.getString("name"));
        }
        for (int i = 0; i < names.size() / 2; i++) {
            output.add(names.get(i));
            output.add(names.get(names.size() - 1 - i));
        }
        if (names.size() % 2 != 0) {
            output.add(names.get(names.size() / 2 + 1));
        }
        output.forEach(System.out::println);
    }

    private static Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");
        return DriverManager.getConnection(CONNECTION_URL + DB_NAME, properties);
    }

}
