package com.example.java_db_06_exercise.repository;

import com.example.java_db_06_exercise.model.enitities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
