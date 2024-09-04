package org.study.tracker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.model.Task;
import org.study.tracker.payload.AddTaskRequest;
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
}
