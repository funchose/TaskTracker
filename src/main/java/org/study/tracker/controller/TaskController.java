package org.study.tracker.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.Role;
import static org.study.tracker.Role.ROLE_ADMIN;
import static org.study.tracker.Role.ROLE_GROUP_MODERATOR;
import org.study.tracker.model.Task;
import org.study.tracker.model.User;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.payload.EditTaskRequest;
import org.study.tracker.responses.TaskResponse;
import org.study.tracker.service.TaskService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Operations with tasks")
public class TaskController {

  private final TaskService taskService;

  @GetMapping("/tasks")
  @Operation(summary = "Get list of all existing tasks",
      description = "Returns a list of all tasks")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "List of tasks is returned"),
      @ApiResponse(responseCode = "403",
          description = "You don't have permission to perform this operation. "
              + "Please, get authorized first"),
      @ApiResponse(responseCode = "404",
          description = "No task is created yet, don't hesitate to add a new one!")})
  public List<Task> getTasks() {
    return taskService.getTasks();
  }

  @Transactional
  @PostMapping("/tasks")
  @Operation(summary = "Create a task",
      description = "Creates a task with non-null author id, name, description, default status"
          + "(OPEN) and current creation date")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task is created"),
      @ApiResponse(responseCode = "403", description = "You don't have permission to perform "
          + "this operation. Please, get authorized first"),
  })
  TaskResponse createTask(@RequestBody AddTaskRequest request,
                          @AuthenticationPrincipal User user) {
    return taskService.createTask(request, user.getId());
  }

  @Transactional
  @PutMapping("/tasks/{id}")
  @Operation(summary = "Edit a task with id",
      description = "Editing the task with the required id: name, description, status",
      responses = @ApiResponse(responseCode = "200", description = "Task is edited"))
  ResponseEntity<TaskResponse> editTask(@PathVariable Long id,
                                        @RequestBody EditTaskRequest request,
                                        @AuthenticationPrincipal User user) {
    if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(Role.ROLE_USER.getName())) {
      var result = taskService.editTaskByUser(id, request.getName(), request.getDescription(),
          request.getStatus(), user.getId());
      //if the other fields are changed - return warning about rights
      return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
      //!don't get badrequest (200 instead) when adding performer,
      // even if only this field has been changed
      //badrequest for editing the other person's task
    } else if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_GROUP_MODERATOR.getName())
        || user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_ADMIN.getName())) {
      var result = taskService.editTaskByModerator(id, request.getName(),
          request.getPerformerId(), request.getDescription(),
          request.getDeadline(), request.getStatus());
      return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    } else {
      TaskResponse taskResponse = new TaskResponse();
      return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
      //therefore, you're a PM and can't edit tasks
    }
  }

  @GetMapping("/tasks/statistics")
  void getStatistics() throws IOException {
    taskService.getStatistics();
  }

  @Transactional
  @DeleteMapping("tasks/{id}")
  @Operation(summary = "Delete a task with id",
      description = "Deleting the task with the required id",
      responses = @ApiResponse(responseCode = "200", description = "Task is deleted"))
  ResponseEntity<TaskResponse> deleteTask(@PathVariable Long id,
                                          @AuthenticationPrincipal User user) {
    if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(Role.ROLE_USER.getName())) {
      var taskForDelete = taskService.deleteTaskByUser(id, user.getId());
      return taskForDelete.map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.badRequest().build());
    } else if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_GROUP_MODERATOR.getName())
        || user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_ADMIN.getName())) {
      var taskForDelete = taskService.deleteTaskByModerator(id);
      return taskForDelete.map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.badRequest().build());
    } else {
      TaskResponse taskResponse = new TaskResponse();
      return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
    }
  }
}
