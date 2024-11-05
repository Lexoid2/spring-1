package com.javarush.kostenko.domain.entity;

import com.javarush.kostenko.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a task in the system.
 * Each task has a description and a status.
 * Mapped to the "task" table in the database.
 */
@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Integer id;

    @Column(nullable = false, length = 100)
    @NonNull
    String description;

    @Enumerated
    @Column(nullable = false)
    @NonNull
    Status status;
}
