import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class P07PrintAllMinionNames {
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = ConnectionSetUp.getConnection();
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

}
