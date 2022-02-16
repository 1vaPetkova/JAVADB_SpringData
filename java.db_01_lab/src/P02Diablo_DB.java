import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class P02Diablo_DB {
    public static void main(String[] args) {
        // 1. Read props from external property file
        Properties props = new Properties();
        String path = P02Diablo_DB.class.getClassLoader()
                .getResource("jdbc.properties")
                .getPath();
        System.out.printf("Resourse path: %s\n", path);
        try {
            props.load(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(props);
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter username (<Enter>) for 'Alex'): ");
        String username = scan.nextLine().trim();
        username = username.length() > 0 ? username : "Alex";
        //2. try with recourses Connection, prepared statement

        try (Connection con = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"))) {
            PreparedStatement ps = con.prepareStatement(props.getProperty("sql.games"));

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            //3. Print results
            while (rs.next()) {
                if (rs.getLong("id") == 0) {
                    System.out.println("No such user exists");
                } else {
                    System.out.println("User: " + username);
                    System.out.printf("%s %s has played %d games\n",
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getLong("count")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
