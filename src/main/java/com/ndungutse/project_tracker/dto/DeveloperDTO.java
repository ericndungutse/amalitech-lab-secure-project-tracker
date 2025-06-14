package com.ndungutse.project_tracker.dto;

import com.ndungutse.project_tracker.model.Developer;
import com.ndungutse.project_tracker.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDTO {
    private Long id;
    private String name;
    private String email;
    private String skills;
    private List<Long> taskIds;

    // Constructor with all parameters is handled by @AllArgsConstructor
    // Default constructor is handled by @NoArgsConstructor

    // Constructor without task ids
    public DeveloperDTO(
            Long id,
            String name,
            String email,
            String skills
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.taskIds = new ArrayList<>();
    }

    // Constructor from Developer entity with tasks ids loaded
    public DeveloperDTO(Developer developer) {
        this.id = developer.getId();
        this.name = developer.getName();
        this.email = developer.getEmail();
        this.skills = developer.getSkills();
        this.taskIds = developer.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
    }

    // Convert Entity to DTO
    public static DeveloperDTO fromEntity(Developer developer) {
        return new DeveloperDTO(developer.getId(), developer.getName(), developer.getEmail(), developer.getSkills());
    }

    // Convert DTO to Entity
    public Developer toEntity() {
        return Developer.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .skills(this.skills)
                .build();
    }

    // Getters and Setters are handled by @Data
}
