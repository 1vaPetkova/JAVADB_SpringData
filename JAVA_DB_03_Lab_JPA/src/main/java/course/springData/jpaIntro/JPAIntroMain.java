package course.springData.jpaIntro;

import course.springData.jpaIntro.entity.Student_new;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAIntroMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("school_jpa");
        EntityManager em = emf.createEntityManager();

//        Student student = new Student("Kiro Kirov");
//        em.getTransaction().begin();
//        em.persist(student);
//        em.getTransaction().commit();


//        em.getTransaction().begin();
//        Student found = em.find(Student.class, 1L);
//        System.out.println("Found student: " + found);
//        em.getTransaction().commit();
//
//        em.getTransaction().begin();
//        em.createQuery("SELECT s FROM Student as s WHERE s.name LIKE ?1")
//                .setParameter(1, "T%")
//                .getResultList()
//                .forEach(System.out::println);
//        em.getTransaction().commit();
//
//        System.out.println("\n----------------------------------------------------\n");
//        em.getTransaction().begin();
//        em.createQuery("SELECT s FROM Student as s WHERE s.name LIKE :name")
//                .setParameter("name", "K%")
//                .getResultList()
//                .forEach(System.out::println);
//        em.getTransaction().commit();

//        em.getTransaction().begin();
//        Student toRemove = em.find(Student.class, 4L);
//        em.remove(toRemove);
//        Student removed = em.find(Student.class, 4L);
//        System.out.println("Removed entity: " + removed);
//        em.getTransaction().commit();


//        em.getTransaction().begin();
//        Student toMerge = em.find(Student.class, 3L);
//        System.out.println(toMerge);
//        em.detach(toMerge);
//        toMerge.setName("Penka Penkova");
//        Student newEntity = em.merge(toMerge);
//        System.out.println(newEntity);
//        em.getTransaction().commit();


        // Lombok example
//        Student_new student = new Student_new("Kiro Kirov");
//        em.getTransaction().begin();
//        em.persist(student);
//        em.getTransaction().commit();


        Student_new student2 = new Student_new("Pesho Goshov");
        em.getTransaction().begin();
        em.persist(student2);
        em.getTransaction().commit();

        em.close();
    }
}
