package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlayerRepository  extends JpaRepository<Player, Integer> {

    //Export all players North Hub
    @Query("SELECT p From Player p WHERE p.team.name = :teamName ORDER BY p.id")
    List<Player> findAllPlayersInGivenTeam(String teamName);

    //Export players with salary bigger than 100000
    List<Player> findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal salary);
}
