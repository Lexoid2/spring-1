package com.javarush.kostenko.service;

import com.javarush.kostenko.dao.TaskRepository;
import com.javarush.kostenko.domain.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing tasks (Task entity).
 * This class provides basic CRUD operations and pagination support for the Task entity.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Retrieves a paginated list of tasks.
     *
     * @param page the page number (0-based index)
     * @param size the number of records per page
     * @return a Page object containing the tasks for the specified page and size
     */
    public Page<Task> findAll(int page, int size) {
        log.info("Retrieving tasks - page: {}, size: {}", page, size);
        return taskRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Finds a task by its ID.
     *
     * @param id the ID of the task to be found
     * @return an Optional containing the found Task, or an empty Optional if no task is found
     */
    public Optional<Task> findById(Integer id) {
        log.info("Looking for task with id: {}", id);
        return taskRepository.findById(id);
    }

    /**
     * Saves a new task or updates an existing one.
     *
     * @param task the task entity to be saved
     */
    public void save(Task task) {
        log.info("Saving task: {}", task);
        taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to be deleted
     */
    public void deleteById(Integer id) {
        log.info("Deleting task with id: {}", id);
        taskRepository.deleteById(id);
    }
}
