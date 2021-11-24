package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.dto.view.PassengerViewDto;
import softuni.exam.models.entities.Passenger;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Passenger findByEmail(String email);

    //Export passengers from data base
    @Query("SELECT new softuni.exam.models.dto.view.PassengerViewDto( p.firstName, p.lastName , " +
            "p.email , p.phoneNumber , COUNT(t.id) ) " +
            "FROM Passenger p LEFT JOIN Ticket t ON p.id = t.passenger.id " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(t.id) DESC, p.email ")
    List<PassengerViewDto> findAllOrderByTicketsCountDescAndEmail();//?

    @Query("SELECT p FROM Passenger p ORDER BY SIZE(p.tickets) DESC, p.email ")
    List<Passenger> findAllOrderedByTicketsCountAndEmail();

}
