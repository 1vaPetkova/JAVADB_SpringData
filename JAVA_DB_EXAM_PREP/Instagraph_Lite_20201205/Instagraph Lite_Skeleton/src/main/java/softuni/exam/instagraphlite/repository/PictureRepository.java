package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entities.Picture;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {

    Boolean existsByPath(String path);
    Picture findByPath(String path);

    //Export Pictures with Size Bigger Than 30000
    List<Picture> findAllBySizeGreaterThanOrderBySizeAsc(Double size);
}
