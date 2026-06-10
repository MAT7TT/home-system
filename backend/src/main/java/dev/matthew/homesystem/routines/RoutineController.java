package dev.matthew.homesystem.routines;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routines")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping
    public List<Routine> getRoutines() {
        return routineService.getRoutines();
    }

    @GetMapping("/{id}")
    public Routine getRoutine(@PathVariable Long id) {
        return routineService.getRoutine(id);
    }

    @GetMapping("/{id}/progress")
    public RoutineProgress getRoutineProgress(@PathVariable Long id) {
        return routineService.getRoutineProgress(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Routine createRoutine(@Valid @RequestBody CreateRoutineRequest request) {
        return routineService.createRoutine(request);
    }

    @PutMapping("/{id}")
    public Routine updateRoutine(@PathVariable Long id, @Valid @RequestBody UpdateRoutineRequest request) {
        return routineService.updateRoutine(id, request);
    }
}