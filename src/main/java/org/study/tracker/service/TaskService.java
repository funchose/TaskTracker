package org.study.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.tracker.model.Task;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
  @Autowired
  TaskRepository taskRepository;

  public List<Task> getTasks() {
    return taskRepository.findAll();
  }

  public Task createTask(AddTaskRequest request) {
    return taskRepository.save(new Task(null, request.getAuthorId(), request.getName(), request.getDescription(), null, null));
  }
}
