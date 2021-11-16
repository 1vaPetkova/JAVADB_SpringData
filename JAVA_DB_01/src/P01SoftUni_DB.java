import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class P01SoftUni_DB {
        public static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
        public static String URL = "jdbc:mysql://localhost:3306/soft_uni";
        public static Connection connection;

        public static void main(String[] args) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter DB username (<Enter>) for 'root'): ");
            String username = scan.nextLine().trim();
            username = username.length() > 0 ? username : "root";
            System.out.println("Enter DB password (<Enter>) for 'root'): ");
            String password = scan.nextLine().trim();
            password = password.length() > 0 ? password : "root";

            //1. Load DB Driver
            try {
                Class.forName(DB_DRIVER);
            } catch (ClassNotFoundException e) {
                System.err.printf("Database driver '%s' not found.", DB_DRIVER);
                System.exit(0);
            }
            System.out.println("DB Driver loaded successfully.");

            //2. Connect to DB
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);
            try {
                connection = DriverManager
                        .getConnection(URL, props);
            } catch (SQLException throwables) {
                System.err.printf("Cannot connect to DB '%s'.", URL);
                System.exit(0);

            }
            System.out.printf("DB connection created successfully: %s\n", URL);

            // 3. Read query parameters
            System.out.println("Enter minimum salary (<Enter>) for '40000'): ");
            String salaryStr = scan.nextLine();
            salaryStr = salaryStr.length() > 0 ? salaryStr : "40000";
            double salary = 40000;
            try {
                salary = Double.parseDouble(salaryStr);
            } catch (NumberFormatException ex) {
                System.out.printf("Invalid number: '%s'\n", salaryStr);
            }

            // 4. Create prepared statement
            PreparedStatement ps;
            try {
                ps = connection
                        .prepareStatement("SELECT * FROM employees where salary > ?");
                //5. Execute prepared statement with parameter:
                ps.setDouble(1, salary);
                ResultSet rs = ps.executeQuery();
                //6. Print
                while (rs.next()) {
                    System.out.printf("| %5d | %-15.15s | %-15.15s | %10.2f |\n",
                            rs.getLong("employee_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDouble("salary")
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 8. Close connection
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.printf("Error closing DB connection %s\n", e.getMessage());
            }
        }
    }
