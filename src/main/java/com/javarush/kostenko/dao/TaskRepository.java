package com.javarush.kostenko.dao;

import com.javarush.kostenko.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing tasks (Task entity).
 * Provides standard CRUD operations and pagination support via Spring Data JPA.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
