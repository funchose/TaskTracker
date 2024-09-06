package org.study.tracker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.model.Task;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.payload.EditTaskRequest;
import org.study.tracker.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  @Autowired
  private TaskService taskService;

  @GetMapping
  ResponseEntity<List<Task>> getTasks() {
    return ResponseEntity.ok(taskService.getTasks());
  }

  @PostMapping
  ResponseEntity<Task> createTask(@RequestBody AddTaskRequest request) {
    return ResponseEntity.ok(taskService.createTask(request));
  }

  @PutMapping("/{taskId}")
  ResponseEntity<Task> editTask(Long taskId, @RequestBody EditTaskRequest request) {
    var result = taskService.editTask(taskId, request.getName(), request.getPerformerId(), request.getDescription(), request.getDeadline(), request.getStatus());
    return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
