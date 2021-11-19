package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {


    @Query("SELECT c FROM Car c ORDER BY SIZE(c.pictures) DESC, c.make")
    List<Car> findAllByOrderByPicturesCountThenMake();
}
