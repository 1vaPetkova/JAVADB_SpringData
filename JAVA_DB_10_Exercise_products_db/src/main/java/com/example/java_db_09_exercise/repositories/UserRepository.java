package com.example.java_db_09_exercise.repositories;

import com.example.java_db_09_exercise.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Q02
    @Query("SELECT u FROM User u WHERE (SELECT COUNT(p) FROM Product p " +
            "WHERE p.seller.id = u.id AND p.buyer IS NOT NULL) > 0 " +
            "ORDER BY u.lastName, u.firstName")
    List<User> findAllUsersBySoldProductsOrderByLastNameThenFirstName();

    //Q04
    @Query("SELECT u FROM User u JOIN Product p ON u.id = p.seller.id " +
            "WHERE p.buyer IS NOT NULL " +
            "GROUP BY u.id " +
            "ORDER BY u.soldProducts.size DESC, u.lastName")
    List<User> findAllUsersWithSoldProductsOrderByProductsCountThenByLastName();
}
