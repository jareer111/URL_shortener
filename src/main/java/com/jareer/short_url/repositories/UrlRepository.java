package com.jareer.short_url.repositories;

import com.jareer.short_url.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    @Query("select u from Url u where u.code = ?1 and u.deleted = false")
    Optional<Url> findByCode(String code);

    @Query("select u from Url u where u.createdBy = ?1")
    List<Url> findByCreatedBy(Long createdBy);

    @Query("select u from Url u where u.createdAt between :createdAtStart and :createdAtEnd order by u.createdAt")
    List<Url> weeklyReport(@Param("createdAtStart") LocalDateTime createdAtStart,
                           @Param("createdAtEnd") LocalDateTime createdAtEnd);


}