package com.javarush.kostenko.service;

import com.javarush.kostenko.dao.TaskRepository;
import com.javarush.kostenko.domain.entity.Task;
import com.javarush.kostenko.domain.enums.Status;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Task> tasks = Arrays.asList(
                new Task("Task 1", Status.IN_PROGRESS),
                new Task("Task 2", Status.DONE)
        );
        Page<Task> page = new PageImpl<>(tasks);
        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Task> result = taskService.findAll(0, 10);

        assertEquals(2, result.getContent().size());
        assertEquals("Task 1", result.getContent().get(0).getDescription());
        verify(taskRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindByIdSuccess() {
        Task task = new Task("Sample Task", Status.IN_PROGRESS);
        setTaskId(task);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Sample Task", result.get().getDescription());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findById(1);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testSave() {
        Task task = new Task("New Task", Status.PAUSED);

        taskService.save(task);

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteById() {
        taskService.deleteById(1);

        verify(taskRepository, times(1)).deleteById(1);
    }

    private void setTaskId(Task task) {
        try {
            var idField = Task.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(task, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
