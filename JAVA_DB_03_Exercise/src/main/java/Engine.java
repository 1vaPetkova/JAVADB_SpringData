import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Engine implements Runnable {
    private final EntityManager em;
    private BufferedReader reader;


    public Engine(EntityManager entityManager) {
        this.em = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Select exercise number: ");
        int exNum = 0;
        em.getTransaction().begin();
        try {
            exNum = Integer.parseInt(reader.readLine());
            switch (exNum) {
                case 2 -> p02ChangeCasing();
                case 3 -> p03ContainsEmployee();
                case 4 -> p04EmployeesWithSalaryOver50000();
                case 5 -> p05EmployeesFromDepartment();
                case 6 -> p06AddingANewAddressAndUpdatingEmployee();
                case 7 -> p07AddressesWithEmployeeCount();
                case 8 -> p08GetEmployeeWithProject();
                case 9 -> p09FindLatest10Projects();
                case 10 -> p10IncreaseSalaries();
                case 11 -> p11FindEmployeesByFirstName();
                case 12 -> p12EmployeesMaximumSalaries();
                case 13 -> p13RemoveTowns();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void p13RemoveTowns() throws IOException {
        System.out.println("Enter town to be removed: ");
        String townToRemove = reader.readLine();
        Town town = em.createQuery("SELECT t FROM Town t WHERE t.name = :town_to_remove", Town.class)
                .setParameter("town_to_remove", townToRemove)
                .getSingleResult();

        List<Address> addresses = em
                .createQuery("SELECT a FROM Address AS a WHERE a.town.id = :town_id", Address.class)
                .setParameter("town_id", town.getId())
                .getResultList();

        int deletedAddresses = addresses.size();
        System.out.printf("%d %s in %s deleted\n",
                deletedAddresses,
                deletedAddresses == 1 ? "address" : "addresses",
                townToRemove
        );

        List<Employee> employees = em
                .createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();
        employees.forEach(e->e.setAddress(null));

        addresses.forEach(a -> {
            a.getEmployees().forEach(е -> е.setAddress(null));
            a.setTown(null);
        });
        addresses.forEach(this.em::remove);
        this.em.remove(town);

        em.getTransaction().commit();
    }


    @SuppressWarnings(value = "unchecked")
    private void p12EmployeesMaximumSalaries() {
//        em.createNativeQuery("SELECT d.name, MAX(e.salary) as m_salary\n" +
//                "FROM departments d\n" +
//                "JOIN employees e ON d.department_id = e.department_id\n" +
//                "GROUP BY d.department_id\n" +
//                "HAVING m_salary NOT BETWEEN 30000 AND 70000;").getResultList();

        em.createQuery("SELECT concat(e.department.name,' ',MAX(e.salary)) " +
                        "FROM Employee AS e " +
                        "GROUP BY e.department.id " +
                        "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000")
                .getResultList()
                .forEach(System.out::println);
        em.getTransaction().commit();
    }

    private void p11FindEmployeesByFirstName() throws IOException {
        System.out.println("Enter pattern: ");
        String pattern = reader.readLine() + "%";
        em.createQuery("SELECT e FROM Employee AS e WHERE e.firstName LIKE :pattern ", Employee.class)
                .setParameter("pattern", pattern)
                .getResultList()
                .forEach(e -> System.out.printf("%s %s - %s - ($%.2f)\n"
                        , e.getFirstName(), e.getLastName(), e.getJobTitle(), e.getSalary()));
        em.getTransaction().commit();
    }

    private void p10IncreaseSalaries() {
        Set<String> departments = Set.of("Engineering", "Tool Design", "Marketing", "Information Services");
        em.createQuery("SELECT e FROM Employee AS e WHERE e.department.name IN :departments", Employee.class)
                .setParameter("departments", departments)
                .getResultList().forEach(e -> {
                    e.setSalary(e.getSalary().multiply(BigDecimal.valueOf(1.12)));
                    System.out.printf("%s %s ($%.2f)\n", e.getFirstName(), e.getLastName(), e.getSalary());
                });
        em.getTransaction().commit();
    }

    private void p09FindLatest10Projects() {
        List<Project> projects = em.createQuery("SELECT p FROM Project AS p ORDER BY p.startDate DESC ", Project.class)
                .setMaxResults(10).getResultList();
        projects.stream().sorted(Comparator.comparing(Project::getName)).forEach(p -> {
            System.out.printf("Project name: %s\n", p.getName());
            System.out.printf("\tProject Description:%s\n", p.getDescription());
            System.out.printf("\tProject Start Date:%s\n", p.getStartDate());
            System.out.printf("\tProject End Date:%s\n", p.getEndDate());
        });
        em.getTransaction().commit();
    }

    private void p08GetEmployeeWithProject() throws IOException {
        System.out.println("Enter employee id: ");
        Integer id = Integer.parseInt(reader.readLine());
        Employee employee = em.createQuery("SELECT e FROM Employee AS e WHERE e.id = :emp_id", Employee.class)
                .setParameter("emp_id", id)
                .getSingleResult();
        System.out.printf("%s %s - %s\n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
        employee.getProjects().stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.printf("    %s\n", p.getName()));
        em.getTransaction().commit();
    }

    private void p07AddressesWithEmployeeCount() {
        List<Address> addresses = em
                .createQuery("SELECT a FROM Address AS a ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses.forEach(a -> System.out.printf("%s, %s - %d employees\n",
                a.getText(), a.getTown() == null ? "Unknown" : a.getTown(), a.getEmployees().size()));
        em.getTransaction().commit();
    }

    private void p06AddingANewAddressAndUpdatingEmployee() throws IOException {
        //Create the new Address:
        Address newAddress = new Address();
        newAddress.setText("Vitoshka 15");
        em.persist(newAddress);
        em.getTransaction().commit();

        //Find the employee with the given last name
        em.getTransaction().begin();
        System.out.println("Enter last name:");
        String lastName = reader.readLine();

        Employee employee = em
                .createQuery("SELECT e FROM Employee AS e WHERE e.lastName = :last_name", Employee.class)
                .setParameter("last_name", lastName)
                .getSingleResult();
        System.out.println(employee.getFirstName() + " " + employee.getLastName() + " " + employee.getAddress().getText());

        //Update the employee address
        employee.setAddress(newAddress);

        System.out.println(employee.getFirstName() + " " + employee.getLastName() + " " + employee.getAddress().getText());
        em.getTransaction().commit();
    }

    private void p05EmployeesFromDepartment() {
        em.createQuery("SELECT e " +
                        "FROM Employee AS e " +
                        "JOIN Department AS d " +
                        "ON e.department = d WHERE d.name = :d_name ORDER BY e.salary, e.id", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultList()
                .forEach(e -> System.out.printf("%s %s from %s - $%.2f\n",
                        e.getFirstName(), e.getLastName(), e.getDepartment().getName(), e.getSalary()));
        em.getTransaction().commit();
    }


    private void p04EmployeesWithSalaryOver50000() {
        em.createQuery("SELECT e FROM Employee AS e WHERE e.salary> :min_salary", Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
        em.getTransaction().commit();
    }

    private void p03ContainsEmployee() throws IOException {
        System.out.println("Enter employee full name: ");
        String[] fullName = reader.readLine().split("\\s+");
        String firstName = fullName[0];
        String lastName = fullName[1];
        Long result = em
                .createQuery("SELECT COUNT(e) FROM Employee e WHERE e.firstName = :f_name AND e.lastName = :l_name"
                        , Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();
        System.out.println(result != 0 ? "Yes" : "No");
        em.getTransaction().commit();
    }

    private void p02ChangeCasing() {
        Query query = em.createQuery("UPDATE Town t SET t.name = UPPER(t.name) WHERE LENGTH(t.name)<=5");
        System.out.println(query.executeUpdate());
        em.getTransaction().commit();
    }

}
