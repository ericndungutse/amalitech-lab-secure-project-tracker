package com.ndungutse.project_tracker.controller;

import com.ndungutse.project_tracker.dto.TaskDTO;
import com.ndungutse.project_tracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task", description = "Task management APIs")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create a new task
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @Parameter(description = "Task data to create", required = true) @RequestBody TaskDTO taskDTO) {
        Optional<TaskDTO> createdTask = taskService.create(taskDTO);
        return createdTask.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // Get all tasks
    @Operation(summary = "Get all tasks", description = "Returns a list of all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Get a task by ID
    @Operation(summary = "Get a task by ID", description = "Returns a task based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(
            @Parameter(description = "ID of the task to retrieve", required = true) @PathVariable Long id) {
        Optional<TaskDTO> task = taskService.getById(id);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a task
    @Operation(summary = "Update a task", description = "Updates a task with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @Parameter(description = "ID of the task to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated task data", required = true) @RequestBody TaskDTO taskDTO) {
        if (!taskService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<TaskDTO> updatedTask = taskService.update(id, taskDTO);
        return updatedTask.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    // Delete a task
    @Operation(summary = "Delete a task", description = "Deletes a task based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true) @PathVariable Long id) {
        if (!taskService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
