package ru.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cinema.model.Hall;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByName(String name);
    boolean existsByName(String name);
    List<Hall> findByNameContainingIgnoreCase(String name);
    List<Hall> findAllByOrderByNameAsc();
    List<Hall> findAllByOrderByCapacityAsc();
}

