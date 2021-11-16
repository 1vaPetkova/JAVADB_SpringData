import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class P03GetMinionNames {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = getConnection();
        System.out.println("Enter villain id: ");
        int villainId = Integer.parseInt(reader.readLine());
        String villainName = findEntityNameById("villains", villainId);
        if (villainName == null) {
            System.out.printf("No villain with ID %d exists in the database.\n", villainId);
            return;
        }
        System.out.println("Villain: " + villainName);

        PreparedStatement ps = connection.prepareStatement(
                "SELECT m.name, m.age\n" +
                        "FROM minions AS m\n" +
                        "JOIN minions_villains mv on m.id = mv.minion_id\n" +
                        "WHERE mv.villain_id = ?;");
        ps.setInt(1, villainId);
        ResultSet rs = ps.executeQuery();
        int index = 1;
        while (rs.next()) {
            System.out.printf("%d. %s %d\n", index++, rs.getString("name"), rs.getInt("age"));
        }

    }

    private static String findEntityNameById(String tableName, int villainId) throws SQLException {
        PreparedStatement psName = connection.prepareStatement("SELECT name FROM " + tableName + " WHERE id = ?;");
        psName.setInt(1, villainId);
        ResultSet rs = psName.executeQuery();
        if (rs.isBeforeFirst()) {
            rs.next();
            return rs.getString("name");
        }
        return null;
    }



    private static Connection getConnection() throws IOException, SQLException {
//        System.out.println("Enter user: ");
//        String user = reader.readLine();
//        System.out.println("Enter password: ");
//        String password = reader.readLine();
//        user = user.length() > 0 ? user : "root";
//        password = password.length() > 0 ? password : "";
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");
        return DriverManager.getConnection(CONNECTION_URL + DB_NAME, properties);
    }
}
