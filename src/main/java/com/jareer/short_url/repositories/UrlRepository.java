package com.jareer.short_url.repositories;

import com.jareer.short_url.entities.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<URL, Long> {
    @Query("select u from URL u where u.code = ?1 and u.deleted = false")
    Optional<URL> findByCode(String code);

    @Query("select u from URL u where u.createdBy = ?1")
    List<URL> findByCreatedBy(Long createdBy);

    @Query("select u from URL u where u.createdAt between :createdAtStart and :createdAtEnd order by u.createdAt")
    List<URL> weeklyReport(@Param("createdAtStart") LocalDateTime createdAtStart,
                           @Param("createdAtEnd") LocalDateTime createdAtEnd);


}