import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class P09IncreaseAgeStoredProcedure {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = ConnectionSetUp.getConnection();

        System.out.println("Enter minion id:");
        int id = Integer.parseInt(reader.readLine());
        CallableStatement cs = connection.prepareCall("CALL usp_get_older(?)");
        cs.setInt(1, id);
        cs.execute();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT concat(name,' ', age) as Output FROM minions WHERE id = ?"
        );
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println(rs.getString("Output"));
        }
    }
}
