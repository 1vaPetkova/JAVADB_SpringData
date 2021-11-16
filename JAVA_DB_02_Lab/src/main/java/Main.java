import entity.User;
import orm.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String DB_NAME = "fsd";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        MyConnector.createConnection(USER, PASSWORD, DB_NAME);
        Connection connection = MyConnector.getConnection();

        EntityManager<User> entityManager = new EntityManager<>(connection);
        System.out.println("Connected to database.");

        User user = new User("999999", "!!!!!!!!", 42, LocalDate.of(2000, 11, 1));
        //user.setId(15);
        entityManager.persist(user);
        System.out.println(entityManager.findFirst(User.class, "username like '%9'"));
        System.out.println(entityManager.findById(User.class, 17));

        System.out.printf("Number of deleted records: %d%n", entityManager.delete(User.class, 2));


        System.out.println("--------------------------------\nUsers above 45:");
        List<User> usersAbove40 = (List<User>) entityManager.find(User.class, "age > 45 ;");
        usersAbove40.forEach(System.out::println);
        System.out.println();
        System.out.println("--------------------------------\nAll users:");
        List<User> users = (List<User>) entityManager.find(User.class);
        users.forEach(System.out::println);

    }

}