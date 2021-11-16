import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class P06RemoveVillain {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = getConnection();
        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());
        String villainName = checkIfVillainExists(villainId);
        if (villainName == null) {
            System.out.println("No such villain was found");
            return;
        }
        int removedMinions = removeMinionsFromVillain(villainId);
        removeVillainById(villainId);
        System.out.println(villainName + " was deleted");
        System.out.println(removedMinions + " minions released");
    }

    private static String checkIfVillainExists(int villainId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        ps.setInt(1, villainId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        }
        return null;
    }

    private static void removeVillainById(int villainId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM villains WHERE id = ?");
        ps.setInt(1, villainId);
        ps.execute();
    }

    private static int removeMinionsFromVillain(int villainId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM minions_villains WHERE villain_id = ?"
        );
        ps.setInt(1, villainId);
        return ps.executeUpdate();
    }

    private static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "бебето123");
        return DriverManager.getConnection(CONNECTION_URL + DB_NAME, properties);
    }
}
