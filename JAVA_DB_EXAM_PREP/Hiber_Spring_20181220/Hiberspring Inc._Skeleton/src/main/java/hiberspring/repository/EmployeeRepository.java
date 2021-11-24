package hiberspring.repository;

import hiberspring.domain.dtos.view.EmployeeViewDto;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByCard(EmployeeCard employeeCard);

    //Productive Employees
    @Query("SELECT new hiberspring.domain.dtos.view.EmployeeViewDto(CONCAT(e.firstName,' ',e.lastName) , " +
            "e.position  , e.card.number)" +
            "FROM Employee e " +
            "WHERE SIZE(e.branch.products) > 0 " +
            "ORDER BY CONCAT(e.firstName,' ',e.lastName), LENGTH(e.position) DESC ")
    List<EmployeeViewDto> findAllByBranchWithProducts();
}
