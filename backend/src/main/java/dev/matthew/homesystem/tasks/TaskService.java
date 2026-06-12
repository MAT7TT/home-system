package dev.matthew.homesystem.tasks;

import dev.matthew.homesystem.routines.Routine;
import dev.matthew.homesystem.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public List<Task> getActiveTasks() {
        return taskRepository.findActive();
    }

    public List<Task> getTodayTasks() {
        return taskRepository.findToday();
    }

    public Task createTask(CreateTaskRequest request) {
        return taskRepository.create(request);
    }

    public Task updateTask(Long id, UpdateTaskRequest request) {
        return taskRepository.update(id, request);
    }

    public Task completeTask(Long id) {
        return taskRepository.complete(id);
    }

    public Task skipTask(Long id, SkipTaskRequest request) {
        return taskRepository.skip(id, request);
    }

    public void deleteTask(Long id) {
        getTask(id);
        taskRepository.delete(id);
    }

    public Task reopenTask(Long id) {
        getTask(id);
        return taskRepository.reopen(id);
    }

    public void linkRoutine(Long taskId, Long routineId) {
        getTask(taskId);
        taskRepository.linkRoutine(taskId, routineId);
    }

    public void unlinkRoutine(Long taskId, Long routineId) {
        getTask(taskId);
        taskRepository.unlinkRoutine(taskId, routineId);
    }

    public List<Routine> getTaskRoutines(Long taskId) {
        getTask(taskId);
        return taskRepository.findRoutinesForTask(taskId);
    }

    public void linkTag(Long taskId, Long tagId) {
        getTask(taskId);
        taskRepository.linkTag(taskId, tagId);
    }

    public void unlinkTag(Long taskId, Long tagId) {
        getTask(taskId);
        taskRepository.unlinkTag(taskId, tagId);
    }

    public List<Tag> getTaskTags(Long taskId) {
        getTask(taskId);
        return taskRepository.findTagsForTask(taskId);
    }
}
