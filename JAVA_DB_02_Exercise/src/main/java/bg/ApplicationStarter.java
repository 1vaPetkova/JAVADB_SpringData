package bg;


import bg.entity.User;
import ormFramework.core.EntityManager;
import ormFramework.core.EntityManagerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * 1) Класче (EntityManagerFactory), в което да регистрираме настройките за връзка с базата данни
 * - вид база, потребителско име
 * и парола, както и името на базата данни. Това класче в ответ ще върне обект, който ще може да управлява
 * на абстрактно ниво базата данни.
 * <p>
 * 2) Преди да се върне такъв обект, искаме да обиколим всички класове в проекта и да намерим тези, които са
 * анотирани с анотацията @Entity (това ще е наша анотация)
 * <p>
 * 3) Ще гледаме дали такава таблица има в базата, ако няма - ще я създадем по предварително описани @Id
 * и @Column(име, тип)
 * <p>
 * 4) Класчето, което ще се върне от т.1. (EntityManager) ще има операции за извличане на обект по ИД
 * find(id, Class); записване на нов или редакция на съществуващ обект persist(Object) и изтриване на обект
 * по ИД delete(id, Class)
 */
public class ApplicationStarter {
    public static void main(String[] args) throws SQLException, URISyntaxException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        EntityManager entityManager = EntityManagerFactory.create(
                "mysql",
                "localhost",
                3306,
                "root",
                "12345",
                "test_orm",
                ApplicationStarter.class
        );

        System.out.println();
//        User user = new User("Pesho1", 17);
//        User maria = new User("Maria", 25);
        //    User kireto = new User("Maria", 25,"1200 Sofia, Mladost");
//
        //  entityManager.persist(user);
//        System.out.println();
        //User pesho = entityManager.findById(1, User.class);
        // User maria = entityManager.findById(3, User.class);
//        pesho.setAge(30);
//        entityManager.persist(user);

        //  maria.setAddress("Sofia 1000");
        //entityManager.delete(maria);
//        User user = new User();


//
//        Address softUniAddress = entityManager.findById(1, Address.class);
//        Address codexioAddress = entityManager.findById(2, Address.class);
//        Department byId1 = entityManager.findById(30, Department.class);


        entityManager.alterTable(User.class);

    }
}
