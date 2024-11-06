package com.javarush.kostenko.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.kostenko.config.TestControllerConfig;
import com.javarush.kostenko.domain.entity.Task;
import com.javarush.kostenko.domain.enums.Status;
import com.javarush.kostenko.service.TaskService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestControllerConfig.class)
@WebAppConfiguration
@FieldDefaults(level = AccessLevel.PRIVATE)
class TaskControllerIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    TaskService taskService;

    MockMvc mockMvc;
    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllTasks() throws Exception {
        taskService.save(new Task("Task 1", Status.IN_PROGRESS));
        taskService.save(new Task("Task 2", Status.DONE));

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("pageSize"));
    }

    @Test
    void testCreateTask() throws Exception {
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("description", "New Task");
        taskData.put("status", Status.IN_PROGRESS);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task created successfully!"));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task task = new Task("Old Task", Status.PAUSED);
        taskService.save(task);

        Map<String, Object> updatedTaskData = new HashMap<>();
        updatedTaskData.put("description", "Updated Task");
        updatedTaskData.put("status", Status.DONE);

        mockMvc.perform(put("/tasks/edit/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskData)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteTask() throws Exception {
        Task task = new Task("Task to Delete", Status.IN_PROGRESS);
        taskService.save(task);

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isNoContent());
    }
}
