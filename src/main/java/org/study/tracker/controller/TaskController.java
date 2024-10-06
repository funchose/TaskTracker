package org.study.tracker.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.Role;
import org.study.tracker.model.Task;
import org.study.tracker.model.User;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.payload.EditTaskRequest;
import org.study.tracker.responses.TaskResponse;
import org.study.tracker.service.TaskService;

import java.io.IOException;
import java.util.List;

import static org.study.tracker.Role.ROLE_ADMIN;
import static org.study.tracker.Role.ROLE_GROUP_MODERATOR;


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

  @Transactional
  @PostMapping("/tasks")
  @Operation(summary = "Create a task",
      description = "Creates a task with non-null author id, name, description, default status" +
          "(OPEN) and current creation date")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task is created"),
      @ApiResponse(responseCode = "403", description = "You don't have permission to perform this operation. " +
          "Please, get authorized first"),
  })
  TaskResponse createTask(@RequestBody AddTaskRequest request, @AuthenticationPrincipal User user) {
    return taskService.createTask(request, user);
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
      var result = taskService.editTaskByUser(id, request.getName(), request.getDescription(), request.getStatus(), user); //если правим другие поля, то нужно вернуть сообщение, что мы не имеем права
      return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build()); // !!!!не получаю бедреквест (вместо него - 200) при добавлении перформера, даже если меняю только его, но все остальное остается
      //badrequest на изменение чужой таски
    } else if (user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
        .contains(ROLE_GROUP_MODERATOR.getName()) ||
        user.getAuthorities().toString().replaceAll("[\\[\\]]", "")
            .contains(ROLE_ADMIN.getName())) {
      var result = taskService.editTaskByModerator(id, request.getName(), request.getPerformerId(), request.getDescription(),
          request.getDeadline(), request.getStatus());
      return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build()); // тут все ок, все параметры меняются
    } else {
      TaskResponse taskResponse = new TaskResponse();
      return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST); //ТУТ ПОЛУЧАЕМ БЭД РЕКВЕСТ, КАК НУЖНО
      // Optional.of(taskResponse).map(ResponseEntity::).orElseGet(() -> ResponseEntity.badRequest().build());
      //значит ты PM, и не можешь редачить таски
    }
    //return task.map(ResponseEntity.).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/tasks/statistics")
  void getStatistics() throws IOException {
    taskService.getStatistics();
  }

  @Transactional
  @ApiResponses(value = {
//      @ApiResponse(responseCode = "200", description = "Task is created"),
//      @ApiResponse(responseCode = "403", description = "You don't have permission to perform this operation. " +
//          "Please, get authorized first"),
  })
  @DeleteMapping("tasks/{id}")
  TaskResponse deleteTask(@PathVariable Long id) {
    return taskService.deleteTask(id);
  }
}
