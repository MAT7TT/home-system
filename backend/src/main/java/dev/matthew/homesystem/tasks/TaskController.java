package dev.matthew.homesystem.tasks;

import dev.matthew.homesystem.routines.Routine;
import dev.matthew.homesystem.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @GetMapping("/active")
    public List<Task> getActiveTasks() {
        return taskService.getActiveTasks();
    }

    @GetMapping("/today")
    public List<Task> getTodayTasks() {
        return taskService.getTodayTasks();
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @PatchMapping("/{id}/complete")
    public Task completeTask(@PathVariable Long id) {
        return taskService.completeTask(id);
    }

    @PatchMapping("/{id}/skip")
    public Task skipTask(@PathVariable Long id, @Valid @RequestBody SkipTaskRequest request) {
        return taskService.skipTask(id, request);
    }

    @PostMapping("/{taskId}/routines/{routineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void linkRoutine(@PathVariable Long taskId, @PathVariable Long routineId) {
        taskService.linkRoutine(taskId, routineId);
    }

    @GetMapping("/{taskId}/routines")
    public List<Routine> getTaskRoutines(@PathVariable Long taskId) {
        return taskService.getTaskRoutines(taskId);
    }

    @DeleteMapping("/{taskId}/routines/{routineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkRoutine(@PathVariable Long taskId, @PathVariable Long routineId) {
        taskService.unlinkRoutine(taskId, routineId);
    }

    @GetMapping("/{taskId}/tags")
    public List<Tag> getTaskTags(@PathVariable Long taskId) {
        return taskService.getTaskTags(taskId);
    }

    @PostMapping("/{taskId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void linkTag(@PathVariable Long taskId, @PathVariable Long tagId) {
        taskService.linkTag(taskId, tagId);
    }

    @DeleteMapping("/{taskId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkTag(@PathVariable Long taskId, @PathVariable Long tagId) {
        taskService.unlinkTag(taskId, tagId);
    }
}
