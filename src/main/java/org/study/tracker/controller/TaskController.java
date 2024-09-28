package org.study.tracker.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.model.Task;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.payload.EditTaskRequest;
import org.study.tracker.responses.TaskResponse;
import org.study.tracker.service.TaskService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Tag(name = "Operations with tasks")
public class TaskController {

  private final TaskService taskService;

  @GetMapping("/tasks")
  @Operation(summary = "Get list of all existing tasks", description = "Returns a list of all tasks")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of tasks is returned"),
      @ApiResponse(responseCode = "403", description = "You don't have permission to perform this operation. " +
          "Please, get authorized first"),
      @ApiResponse(responseCode = "404", description = "No task is created yet, don't hesitate to add a new one!")})
  public List<Task> getTasks() {
    return taskService.getTasks();
  }

  @PostMapping("/tasks")
  @Operation(summary = "Create a task",
      description = "Creates a task with non-null author id, name, description, default status" +
          "(OPEN) and current creation date")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task is created"),
      @ApiResponse(responseCode = "403", description = "You don't have permission to perform this operation. " +
          "Please, get authorized first"),
  })
  TaskResponse createTask(@RequestBody AddTaskRequest request) {
    return taskService.createTask(request);
  }

  @PutMapping("/tasks/{id}")
  @Operation(summary = "Edit a task with id",
      description = "Editing the task with the required id",
      responses = @ApiResponse(responseCode = "200", description = "Task is edited"))
  ResponseEntity<TaskResponse> editTask(@PathVariable Long id, @RequestBody EditTaskRequest request) {
    var result = taskService.editTask(id, request.getName(), request.getPerformerId(), request.getDescription(),
        request.getDeadline(), request.getStatus());
    Optional<TaskResponse> task = Optional.of(new TaskResponse(result.getId()));
    return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/tasks/statistics")
  void getStatistics() throws IOException {
    taskService.getStatistics();
  }
}
