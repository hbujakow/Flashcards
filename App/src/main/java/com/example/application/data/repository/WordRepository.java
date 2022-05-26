package com.example.application.data.repository;

import com.example.application.data.entity.Slowko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository extends JpaRepository<Slowko, Long> {

    @Query("select c from Slowko c " +
            "where lower(c.kategoria.nameOfTheCategory) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.word) like lower(concat('%', :searchTerm, '%'))")
    List<Slowko> search(@Param("searchTerm") String searchTerm);

}
