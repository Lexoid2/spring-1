package com.javarush.kostenko.controller;

import com.javarush.kostenko.domain.entity.Task;
import com.javarush.kostenko.domain.enums.Status;
import com.javarush.kostenko.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This controller handles CRUD operations for tasks.
 * It supports listing, creating, updating, and deleting tasks.
 * Each endpoint specifies response codes for successful and error cases.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    /**
     * Redirects the root URL to the task list page.
     *
     * @return redirect URL to "/tasks".
     */
    @GetMapping("/")
    public String redirectToTasks() {
        return "redirect:/tasks";
    }

    /**
     * Retrieves a paginated list of tasks and displays them in the view.
     *
     * @param page the page number to retrieve, defaults to 0.
     * @param size the number of tasks per page, defaults to 10.
     * @param model the model to populate with task data.
     * @return the "tasks" view displaying the paginated list.
     */
    @GetMapping("/tasks")
    public String getAllTasks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        log.info("Fetching tasks - page: {}, size: {}", page, size);
        Page<Task> tasksPage = taskService.findAll(page, size);

        model.addAttribute("tasks", tasksPage.getContent());
        model.addAttribute("pageNumber", tasksPage.getNumber());
        model.addAttribute("totalPages", tasksPage.getTotalPages());
        model.addAttribute("pageSize", tasksPage.getSize());

        return "tasks";
    }

    /**
     * Updates an existing task with new description and status.
     *
     * @param id the ID of the task to update.
     * @param taskUpdate a JSON payload containing updated task details.
     * @return 200 OK if the task is updated successfully,
     *         404 Not Found if the task is not found.
     */
    @PutMapping(value = "/tasks/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @RequestBody Task taskUpdate) {
        log.info("Updating task with id: {}", id);
        Optional<Task> existingTaskOptional = taskService.findById(id);

        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.setDescription(taskUpdate.getDescription());
            existingTask.setStatus(taskUpdate.getStatus());

            taskService.save(existingTask);
            return ResponseEntity.ok().build();
        } else {
            log.warn("Task with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete.
     * @return 204 No Content if the task is deleted successfully,
     *         404 Not Found if the task does not exist.
     */
    @DeleteMapping("/tasks/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with id: {}", id);
        if (taskService.findById(id).isPresent()) {
            taskService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Task with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new task with the given description and status.
     * If the status is not provided, defaults to "IN_PROGRESS".
     *
     * @param newTask a JSON payload with task description and optional status.
     * @return 200 OK if the task is created successfully.
     */
    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createTask(@RequestBody Task newTask) {
        log.info("Creating new task: {}", newTask);
        newTask.setStatus(Optional.of(newTask.getStatus()).orElse(Status.IN_PROGRESS));

        taskService.save(newTask);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Task created successfully!");

        return ResponseEntity.ok(response);
    }
}
