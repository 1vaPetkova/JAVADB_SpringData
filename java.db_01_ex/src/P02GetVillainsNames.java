import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class P02GetVillainsNames {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions";

    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = ConnectionSetUp.getConnection();

        PreparedStatement ps = connection.prepareStatement(
                "SELECT concat_ws(' ', v.name, COUNT(DISTINCT mv.minion_id)) AS Output\n" +
                        "FROM villains AS v\n" +
                        "         JOIN minions_villains mv ON v.id = mv.villain_id\n" +
                        "GROUP BY mv.villain_id\n" +
                        "HAVING COUNT(DISTINCT mv.minion_id) > ?\n" +
                        "ORDER BY COUNT(mv.minion_id) DESC;");
        ps.setInt(1, 15);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("Output"));
        }
    }

}
