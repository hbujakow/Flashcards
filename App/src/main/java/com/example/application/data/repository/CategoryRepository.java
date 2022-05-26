package com.example.application.data.repository;

import com.example.application.data.entity.Kategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Kategoria, Integer> {
    @Query("select c from Kategoria c " +
            "where lower(c.nameOfTheCategory) like lower(concat('%', :searchTerm, '%')) ")
    List<Kategoria> search(@Param("searchTerm") String searchTerm);
}
