import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class P05ChangeTownNamesCasing {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = ConnectionSetUp.getConnection();
        System.out.println("Enter country name:");
        String countryName = reader.readLine();
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE towns SET name = UPPER(name) WHERE country = ?;"
        );
        ps.setString(1, countryName);
        int updateCount = ps.executeUpdate();
        if (updateCount == 0) {
            System.out.println("No town names were affected.");
            return;
        }
        PreparedStatement psPrint = connection.prepareStatement("SELECT name FROM towns WHERE country = ?");
        psPrint.setString(1, countryName);
        ResultSet rs = psPrint.executeQuery();
        List<String> townsChanged = new ArrayList<>();
        while (rs.next()) {
            townsChanged.add(rs.getString("name"));
        }
        System.out.printf("%d town names were affected.\n", updateCount);
        System.out.println(townsChanged);

    }

}
