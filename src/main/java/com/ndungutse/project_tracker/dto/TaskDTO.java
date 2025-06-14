package com.ndungutse.project_tracker.dto;

import com.ndungutse.project_tracker.model.Developer;
import com.ndungutse.project_tracker.model.Project;
import com.ndungutse.project_tracker.model.Task;

import java.time.LocalDate;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private boolean status;
    private LocalDate dueDate;
    private Long projectId;
    private Long developerId;
    private ProjectDTO projectDTO;
    private DeveloperDTO developerDTO;

    // Default constructor
    public TaskDTO() {
    }

    // Constructor with all parameters
    public TaskDTO(
            Long id,
            String title,
            String description,
            boolean status,
            LocalDate dueDate,
            Long projectId,
            Long developerId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.developerId = developerId;
    }

    // Constructor without ID (for creating new tasks)
    public TaskDTO(
            String title,
            String description,
            boolean status,
            LocalDate dueDate,
            Long projectId,
            Long developerId
    ) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.developerId = developerId;
    }

    // Convert Entity to DTO
    public static TaskDTO fromEntity(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDeveloperId(task.getDeveloper().getId());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.isStatus());
        dto.setDueDate(task.getDueDate());
        dto.projectDTO = ProjectDTO.fromEntity(task.getProject());

        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }

        if (task.getDeveloper() != null) {
            dto.setDeveloperDTO(DeveloperDTO.fromEntity(task.getDeveloper()));
        }
        return dto;
    }

    // Convert DTO to Entity
    public Task toEntity(
            Project project,
            Developer developer
    ) {
        return Task.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .status(this.status)
                .dueDate(this.dueDate)
                .project(project)
                .developer(developer)
                .developerId(this.developerId)
                .build();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }

    public ProjectDTO getProjectDTO() {
        return projectDTO;
    }

    public void setProjectDTO(ProjectDTO projectDTO) {
        this.projectDTO = projectDTO;
    }

    public DeveloperDTO getDeveloperDTO() {
        return developerDTO;
    }

    public void setDeveloperDTO(DeveloperDTO developerDTO) {
        this.developerDTO = developerDTO;
    }
}
