package ru.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinema.model.Hall;
import ru.cinema.repository.HallRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HallService {
    private final HallRepository hallRepository;

    @Autowired
    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Optional<Hall> getHallById(Long id) {
        return hallRepository.findById(id);
    }

    public Hall saveHall(Hall hall) {
        if (hallRepository.existsByName(hall.getName())) {
            throw new IllegalArgumentException("Зал с таким названием уже существует");
        }
        return hallRepository.save(hall);
    }

    public Hall updateHall(Hall hall) {
        Hall existingHall = hallRepository.findById(hall.getId())
                .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));
        
        if (!existingHall.getName().equals(hall.getName()) && 
            hallRepository.existsByName(hall.getName())) {
            throw new IllegalArgumentException("Зал с таким названием уже существует");
        }
        
        return hallRepository.save(hall);
    }

    public void deleteHall(Long id) {
        if (!hallRepository.existsById(id)) {
            throw new IllegalArgumentException("Зал не найден");
        }
        hallRepository.deleteById(id);
    }

    public List<Hall> searchHalls(String keyword) {
        return hallRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Hall> sortHallsByName() {
        return hallRepository.findAllByOrderByNameAsc();
    }

    public List<Hall> sortHallsByCapacity() {
        return hallRepository.findAllByOrderByCapacityAsc();
    }
}

