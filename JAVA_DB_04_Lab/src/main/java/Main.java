import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        EntityManager em = Persistence
                .createEntityManagerFactory("vehicles")
                .createEntityManager();
        em.getTransaction().begin();
        Car car = new Car("Audi", "A8", new BigDecimal(56000),
                "hybrid", 5);
        em.persist(car);

        Truck truck = new Truck("Fuso", "Canter",
                new BigDecimal(56000), "hybrid", 5.5);
        em.persist(truck);

        Bike bike = new Bike("Bike", "5",
                new BigDecimal(180), "gas");
        em.persist(bike);

        Plane plane1 = new Plane("Boeing", "737",
                new BigDecimal(180), "gas", 180);
        Plane plane2 = new Plane("AirBus", "662",
                new BigDecimal(180), "kerosene", 162);
        Plane plane3 = new Plane("Aircraft", "133",
                new BigDecimal(180), "gas", 255);

        em.persist(plane1);
        em.persist(plane2);
        em.persist(plane3);

        PlateNumber plateNumber = new PlateNumber("CA0064BB", car);
        em.persist(plateNumber);

        Set<Plane> planes = new HashSet<>();
        planes.add(plane1);
        planes.add(plane2);
        planes.add(plane3);

        Company company = new Company("WizzAir", planes);
        em.persist(company);

        Driver driver1 = new Driver("Pesho Peshov");
        driver1.addVehicle(car);
        driver1.addVehicle(plane2);

        em.persist(driver1);

        Driver driver2 = new Driver("Gosho Goshov");
        driver2.addVehicle(car);
        driver2.addVehicle(truck);

        em.persist(driver2);

        em.getTransaction().commit();
    }
}
