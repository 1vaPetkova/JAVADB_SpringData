
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class P07PrintAllMinionNamesv2 {
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = ConnectionSetUp.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM minions");
        ResultSet rs = ps.executeQuery();
        Deque<String> queue = new ArrayDeque<>();
        Deque<String> stack = new ArrayDeque<>();
        while (rs.next()) {
            queue.offer(rs.getString("name"));
            stack.push(rs.getString("name"));
        }
        int size = queue.size();
        while (queue.size() > size / 2) {
            System.out.println(queue.poll());
            System.out.println(stack.pop());
        }
        if (size % 2 != 0) {
            System.out.println(stack.pop());
        }
    }

}
