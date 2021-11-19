package mostwanted.repository;

import mostwanted.domain.entities.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {

    Town findByName(String name);

    @Query("SELECT t FROM Town t WHERE SIZE(t.racers)>0 ORDER BY SIZE(t.racers) DESC, t.name")
    List<Town> findAllTownsWithRacers();
}
