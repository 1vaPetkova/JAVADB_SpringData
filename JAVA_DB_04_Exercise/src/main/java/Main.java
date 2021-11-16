import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManager em = Persistence
                .createEntityManagerFactory("exercise")
                .createEntityManager();


        em.getTransaction().begin();

        //P02
//     Product product = new Product("sss cheeese", 5.0, BigDecimal.TEN);
//        Customer customer = new Customer("Toshko", "toshkko@gosho.com", "166225225");
//        StoreLocation storeLocation = new StoreLocation("storchee");
//
//        Sale sale1 = new Sale();
//        sale1.setDate(LocalDateTime.now());
//
//        product.getSales().add(sale1);
//        customer.getSales().add(sale1);
//        storeLocation.getSales().add(sale1);
//        sale1.setProduct(product);
//        sale1.setCustomer(customer);
//        sale1.setStoreLocation(storeLocation);
//        em.persist(product);
//        em.persist(customer);
//        em.persist(storeLocation);
//
//       // Product product2 = new Product("Tomato", 6.5, BigDecimal.TEN);
//       // product2.getSales().add(sale);
//
//      //  em.persist(product2);
//
//
////        Product found = em.find(Product.class,3L);
////        em.remove(found);


        //P03

        em.getTransaction().commit();
    }
}
