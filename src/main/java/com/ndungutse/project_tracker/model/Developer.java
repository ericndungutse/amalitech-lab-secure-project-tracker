package com.ndungutse.project_tracker.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "developers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String email;
    private String skills;

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL)
    @ToString.Exclude  // Prevent circular reference in toString()
    @Builder.Default   // Use this default value when using the builder pattern
    private List<Task> tasks = new ArrayList<>();

    // Constructors are handled by @NoArgsConstructor and @AllArgsConstructor

    // Getters and Setters are handled by @Data

    public void addTask(Task task) {
        // Maintain consistency in the current session.
        tasks.add(task);
        task.setDeveloper(this);
    }

    public void removeTask(Task task) {
        // Maintain consistency in the current session.
        tasks.remove(task);
        task.setDeveloper(null);
    }
}
