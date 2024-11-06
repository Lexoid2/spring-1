package com.javarush.kostenko.controller;

import com.javarush.kostenko.domain.entity.Task;
import com.javarush.kostenko.domain.enums.Status;
import com.javarush.kostenko.service.TaskService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class TaskControllerTest {

    @Mock
    TaskService taskService;

    @Mock
    Model model;

    @InjectMocks
    TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRedirectToTasks() {
        String result = taskController.redirectToTasks();
        assertEquals("redirect:/tasks", result);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(
                new Task("Task 1", Status.IN_PROGRESS),
                new Task("Task 2", Status.DONE)
        );
        Page<Task> page = new PageImpl<>(tasks);
        when(taskService.findAll(anyInt(), anyInt())).thenReturn(page);

        String result = taskController.getAllTasks(0, 10, model);

        assertEquals("tasks", result);
        verify(model, times(1)).addAttribute("tasks", tasks);
        verify(model, times(1)).addAttribute("pageNumber", page.getNumber());
        verify(model, times(1)).addAttribute("totalPages", page.getTotalPages());
        verify(model, times(1)).addAttribute("pageSize", page.getSize());
    }

    @Test
    void testUpdateTaskSuccess() throws Exception {
        Task task = new Task("Existing Task", Status.PAUSED);
        setTaskId(task); // Устанавливаем id через рефлексию
        when(taskService.findById(1)).thenReturn(Optional.of(task));

        Task taskUpdate = new Task("Updated Task", Status.DONE);
        ResponseEntity<Void> response = taskController.updateTask(1, taskUpdate);

        assertEquals(ResponseEntity.ok().build(), response);
        assertEquals("Updated Task", task.getDescription());
        verify(taskService, times(1)).save(task);
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskService.findById(1)).thenReturn(Optional.empty());

        Task taskUpdate = new Task("Updated Task", Status.DONE);
        ResponseEntity<Void> response = taskController.updateTask(1, taskUpdate);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(taskService, never()).save(any(Task.class));
    }

    @Test
    void testDeleteTaskSuccess() throws Exception {
        Task task = new Task("Task to Delete", Status.DONE);
        setTaskId(task);
        when(taskService.findById(1)).thenReturn(Optional.of(task));

        ResponseEntity<Void> response = taskController.deleteTask(1);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(taskService, times(1)).deleteById(1);
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskService.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = taskController.deleteTask(1);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(taskService, never()).deleteById(anyInt());
    }

    @Test
    void testCreateTask() {
        Task newTask = new Task("New Task", Status.IN_PROGRESS);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("message", "Task created successfully!");

        ResponseEntity<Map<String, Object>> response = taskController.createTask(newTask);

        assertEquals(ResponseEntity.ok(responseMap), response);
        verify(taskService, times(1)).save(newTask);
    }

    private void setTaskId(Task task) throws Exception {
        Field idField = Task.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(task, 1);
    }
}
