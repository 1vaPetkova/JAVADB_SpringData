package alararestaurant.repository;

import alararestaurant.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    //Q01

    @Query("SELECT c FROM Category c " +
            "JOIN Item i ON i.category.id = c.id " +
            "GROUP BY c.name " +
            "ORDER BY SIZE(c.items) DESC, " +
            "SUM(i.price) DESC")
    List<Category> findAllOrderByCountOfItemsDesc();
}
