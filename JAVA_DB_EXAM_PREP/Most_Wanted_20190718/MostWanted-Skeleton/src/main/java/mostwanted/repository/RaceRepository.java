package mostwanted.repository;

import mostwanted.domain.entities.District;
import mostwanted.domain.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer> {
Race findByLapsAndDistrict(Integer laps, District district);
}
