package org.study.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
  private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

  private final TaskService taskService;

  @GetMapping("/admin/tasks")
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
  public List<TaskResponse> getTasks() {
    return taskService.getTasks();
  }


  @Transactional
  @GetMapping("/tasks")
  @Operation(summary = "Get list of all existing tasks",
      description = "Returns a list of all tasks")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "List of tasks is returned"),
      @ApiResponse(responseCode = "403",
          description = "User doesn't have permission to perform this operation. "
              + "Authorization is required"),
      @ApiResponse(responseCode = "404",
          description = "No task is created yet")})
  public List<Task> getUserTasksToDo(@AuthenticationPrincipal User user) {
    return taskService.getUserTasksToDo(user.getId());
  }

  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
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
    TaskResponse newTask = taskService.createTask(request, user.getId());
    logger.info("Task with id " + newTask.getId() + " was created");
    return newTask;
  }

  @Transactional
  @PutMapping("/tasks/{id}")
  @Operation(summary = "Edit a task with id",
      description = "Editing the task with the required id: name, description, status")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task is edited"),
      @ApiResponse(responseCode = "400", description = "Incorrect data was entered"),
  })
  ResponseEntity<TaskResponse> editTask(@PathVariable Long id,
                                        @RequestBody EditTaskRequest request,
                                        @AuthenticationPrincipal User user) {
    var taskForEdit = Optional.of(new TaskResponse());
    if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(Role.ROLE_USER.getName())) {
      taskForEdit = taskService.editTaskByUser(id, request.getName(), request.getDescription(),
          request.getStatus(), user.getId());

    } else if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_GROUP_MODERATOR.getName())
        || user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_ADMIN.getName())) {
      taskForEdit = taskService.editTaskByModerator(id, request.getName(),
          request.getPerformerId(), request.getDescription(),
          request.getDeadline(), request.getStatus());
    } else {
      TaskResponse taskResponse = new TaskResponse();
      return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
    }
    if (taskForEdit.isPresent()) {
      logger.info("Task with ID " + id + " was edited by user with ID " + user.getId());
      return new ResponseEntity<>(taskForEdit.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/tasks/statistics")
  void getStatistics() throws IOException {
    taskService.getStatistics();
    logger.info("Statistics was collected");
  }

  @Transactional
  @DeleteMapping("tasks/{id}")
  @Operation(summary = "Delete a task with id",
      description = "Deleting the task with the required id",
      responses = @ApiResponse(responseCode = "200", description = "Task is deleted"))
  ResponseEntity<TaskResponse> deleteTask(@PathVariable Long id,
                                          @AuthenticationPrincipal User user) {
    var taskForDelete = Optional.of(new TaskResponse());
    if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(Role.ROLE_USER.getName())) {
      taskForDelete = taskService.deleteTaskByUser(id, user.getId());
    } else if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_GROUP_MODERATOR.getName())
        || user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_ADMIN.getName())) {
      taskForDelete = taskService.deleteTaskByModerator(id);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (taskForDelete.isPresent()) {
      logger.info("Task with ID " + id + " was deleted by user with ID " + user.getId());
      return new ResponseEntity<>(taskForDelete.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
