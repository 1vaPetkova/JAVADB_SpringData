import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

public class P08IncreaseMinionsAge {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = getConnection();
        int[] ids = Arrays.stream(reader.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();
        for (int id : ids) {
            CallableStatement cs = connection.prepareCall("CALL usp_get_older(?)");
            cs.setInt(1, id);
            cs.execute();
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE minions SET name  = LOWER(name) WHERE id = ?;"
            );
            ps.setInt(1, id);
            ps.execute();
        }
        PreparedStatement psOutput = connection.prepareStatement(
                "SELECT concat_ws(' ', name, age) AS output FROM minions"
        );
        ResultSet rs = psOutput.executeQuery();
        while (rs.next()){
            System.out.println(rs.getString("output"));
        }

    }

    private static Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");
        return DriverManager.getConnection(CONNECTION_URL + DB_NAME, properties);
    }
}
