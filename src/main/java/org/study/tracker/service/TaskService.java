package org.study.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.tracker.Status;
import org.study.tracker.model.Task;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.repository.TaskRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
  @Autowired
  TaskRepository taskRepository;

  public List<Task> getTasks() {
    return taskRepository.findAll();
  }

  public Task createTask(AddTaskRequest request) {
    return taskRepository.save(new Task(null, request.getAuthorId(), request.getName(), request.getDescription(), request.getDeadline(), request.getStatus(), request.getPerformerId()));
  }

  public Optional<Task> editTask(Long taskId, String name, Long performerId, String description, ZonedDateTime deadline, Status status) {
    Optional<Task> task = taskRepository.findById(1L);
    //return Optional.of(taskRepository.save(new Task(taskId, name, performerId, description, deadline, status)));
    Task newTask = new Task();
    newTask.setCreationDate(task.get().getCreationDate());
    newTask.setTaskId(task.get().getId());
    newTask.setAuthorId(task.get().getAuthorId());
    if (name != null) {
      newTask.setName(name);
    } else {
      newTask.setName(task.get().getName());
    }
    if (performerId != null) {
      newTask.setPerformerId(performerId);
    } else {
      newTask.setPerformerId(task.get().getPerformerId());
    }
    if (description != null) {
      newTask.setDescription(description);
    } else {
      newTask.setDescription(task.get().getDescription());
    }
    if (deadline != null) {
      newTask.setDeadline(deadline);
    } else {
      newTask.setDeadline(task.get().getDeadline());
    }
    if (status != null) {
      newTask.setStatus(status);
    } else {
      newTask.setStatus(task.map(Task.class::cast).get().getStatus());
    }
    return task.map(value -> taskRepository.save(newTask));
  }
}
