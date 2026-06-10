package dev.matthew.homesystem.routines;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public List<Routine> getRoutines() {
        return routineRepository.findAll();
    }

    public Routine getRoutine(Long id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found"));
    }

    public RoutineProgress getRoutineProgress(Long id) {
        getRoutine(id);
        return routineRepository.getProgress(id);
    }

    public Routine createRoutine(CreateRoutineRequest request) {
        return routineRepository.create(request);
    }

    public Routine updateRoutine(Long id, UpdateRoutineRequest request) {
        return routineRepository.update(id, request);
    }
}