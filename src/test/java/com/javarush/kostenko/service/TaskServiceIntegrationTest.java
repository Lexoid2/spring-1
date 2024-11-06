package com.javarush.kostenko.service;

import com.javarush.kostenko.config.TestServiceConfig;
import com.javarush.kostenko.domain.entity.Task;
import com.javarush.kostenko.domain.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfig.class)
@Transactional
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Test
    void testSaveAndFindById() {
        Task task = new Task("Sample Task", Status.IN_PROGRESS);
        taskService.save(task);

        Optional<Task> foundTask = taskService.findById(task.getId());
        assertTrue(foundTask.isPresent());
        assertEquals("Sample Task", foundTask.get().getDescription());
        assertEquals(Status.IN_PROGRESS, foundTask.get().getStatus());
    }

    @Test
    void testFindAll() {
        taskService.save(new Task("Task 1", Status.IN_PROGRESS));
        taskService.save(new Task("Task 2", Status.DONE));

        var tasksPage = taskService.findAll(0, 10);

        assertEquals(2, tasksPage.getContent().size());
        assertEquals("Task 1", tasksPage.getContent().get(0).getDescription());
        assertEquals(Status.IN_PROGRESS, tasksPage.getContent().get(0).getStatus());
        assertEquals("Task 2", tasksPage.getContent().get(1).getDescription());
        assertEquals(Status.DONE, tasksPage.getContent().get(1).getStatus());
    }

    @Test
    void testDeleteById() {
        Task task = new Task("Task to Delete", Status.DONE);
        taskService.save(task);

        taskService.deleteById(task.getId());
        Optional<Task> deletedTask = taskService.findById(task.getId());
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Initial Task", Status.IN_PROGRESS);
        taskService.save(task);

        task.setDescription("Updated Task");
        task.setStatus(Status.DONE);
        taskService.save(task);

        Optional<Task> updatedTask = taskService.findById(task.getId());
        assertTrue(updatedTask.isPresent());
        assertEquals("Updated Task", updatedTask.get().getDescription());
        assertEquals(Status.DONE, updatedTask.get().getStatus());
    }
}
