package exam.repository;

import exam.model.entities.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    Laptop findByMacAddress(String macAddress);

    //Export Best Laptops from the Data Base
    List<Laptop> findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddress();
}
