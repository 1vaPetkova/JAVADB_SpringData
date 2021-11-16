package course.springData.hibernateIntro;

import course.springData.hibernateIntro.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class HibernateIntroMain {
    public static void main(String[] args) {

        //Create Hibernate config
        Configuration cfg = new Configuration();
        cfg.configure();

        // Create SessionFactory
        SessionFactory sf = cfg.buildSessionFactory();

        // Create Session
        Session session = sf.openSession();

        // Persist an entity
//        Student student = new Student("Gosho Goshov");
//        session.beginTransaction();
//        session.save(student);
//        session.getTransaction().commit();

        // Read entity by Id
//        session.beginTransaction();
//        session.setHibernateFlushMode(FlushMode.MANUAL);
//        Optional<Student> result = session.byId(Student.class).loadOptional(123L);
//        session.getTransaction().commit();
//        System.out.printf("Student -> %s", result.orElseGet(() -> null));

        // List all students using HQL
//        session.beginTransaction();
//        session.createQuery("FROM Student ", Student.class).setFirstResult(3).setMaxResults(10)
//                .stream().forEach(System.out::println);
//        session.getTransaction().commit();


        // Find a student by name using HQL
       session.beginTransaction();
       session.createQuery("FROM Student WHERE name =:name", Student.class)
                .setParameter("name","Gosho Goshov").stream().forEach(System.out::println);
        session.getTransaction().commit();

        //Type-safe criteria queries
        System.out.println("\n------------------------------------------------------------------\n");
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> Student_ = query.from(Student.class);
        query.select(Student_).where(cb.like(Student_.get("name"), "P%"));
        session.createQuery(query).getResultList().forEach(System.out::println);



        // Close session
        session.close();



    }
}
