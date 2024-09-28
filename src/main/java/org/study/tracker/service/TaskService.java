package org.study.tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.study.tracker.Status;
import org.study.tracker.model.Task;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.repository.TaskRepository;
import org.study.tracker.responses.TaskResponse;
import org.study.tracker.utils.StatisticsCollector;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final StatisticsCollector statisticsCollector;

  public List<Task> getTasks() {
    return taskRepository.findAll();
  }

  @Transactional
  public TaskResponse createTask(AddTaskRequest request) {
    Task taskForCreate = new Task(null, request.getAuthorId(), request.getName(), request.getDescription(),
        request.getDeadline(), request.getStatus(), request.getPerformerId());
    var task = taskRepository.save(taskForCreate);
    return new TaskResponse(task.getId());
  }

  @Transactional
  public TaskResponse editTask(Long taskId, String name, Long performerId, String description,
                               ZonedDateTime deadline, Status status) {
    Optional<Task> taskForEdit = taskRepository.findById(taskId);
    Task newTask = new Task();
    newTask.setCreationDate(taskForEdit.get().getCreationDate());
    newTask.setId(taskForEdit.get().getId());
    newTask.setAuthorId(taskForEdit.get().getAuthorId());
    if (name != null) {
      newTask.setName(name);
    } else {
      newTask.setName(taskForEdit.get().getName());
    }
    if (performerId != null) {
      newTask.setPerformerId(performerId);
    } else {
      newTask.setPerformerId(taskForEdit.get().getPerformerId());
    }
    if (description != null) {
      newTask.setDescription(description);
    } else {
      newTask.setDescription(taskForEdit.get().getDescription());
    }
    if (deadline != null) {
      newTask.setDeadline(deadline);
    } else {
      newTask.setDeadline(taskForEdit.get().getDeadline());
    }
    if (status != null) {
      newTask.setStatus(status);
    } else {
      newTask.setStatus(taskForEdit.map(Task.class::cast).get().getStatus());
    }
    var task = taskForEdit.map(value -> taskRepository.save(newTask));
    return new TaskResponse(newTask.getId());
  }

  @Transactional
  public void getStatistics() throws IOException {
    statisticsCollector.getStatuses();
    List<String[]> dataLines = new ArrayList<>();
    dataLines.add(new String[]{
        "Total amount of tasks: " + (long) taskRepository.findAll().size() + "\n"
    });
    for (Status status : statisticsCollector.getStatusSet()) {
      dataLines.add(new String[]{
          "Status: " + status.name() + ", tasks: " + taskRepository.findByStatus(status).stream().count() + "\n"
      });
    }
    statisticsCollector.csvOutput(dataLines);
  }
}
