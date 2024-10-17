package org.study.tracker.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.tracker.Status;
import org.study.tracker.exceptions.TaskNotFoundException;
import org.study.tracker.model.Task;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.repository.TaskRepository;
import org.study.tracker.responses.TaskResponse;
import org.study.tracker.utils.StatisticsCollector;

@Service
@RequiredArgsConstructor
public class TaskService {
  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
  private final TaskRepository taskRepository;
  private final StatisticsCollector statisticsCollector;

  public List<TaskResponse> getTasks() {
    List<TaskResponse> taskResponseList = new ArrayList<>();
    for (Task task : taskRepository.findAll()) {
      TaskResponse taskResponse = new TaskResponse();
      taskResponse.setId(task.getId());
      taskResponse.setName(task.getName());
      taskResponse.setAuthorId(task.getAuthorId());
      taskResponse.setPerformerId(task.getPerformerId());
      taskResponseList.add(taskResponse);
    }
    logger.debug("List of all task responses is received");
    return taskResponseList;
  }

  @Transactional
  public List<Task> getUserTasksToDo(Long userId) {
    List<Task> taskList = taskRepository.findAll();
    var list = taskList.stream().filter(task -> task.getPerformerId().equals(userId))
        .collect(Collectors.toList());
    logger.debug("List of user's tasks is received");
    return list;
  }

  @Transactional
  public TaskResponse createTask(AddTaskRequest request, Long userId) {
    Task taskForCreate = new Task(null, userId, request.getName(),
        request.getDescription(), request.getDeadline(), request.getPerformerId());
    var task = taskRepository.save(taskForCreate);
    return new TaskResponse(task.getId());
  }

  @Transactional
  public Optional<TaskResponse> editTaskByModerator(Long taskId, String name, Long performerId,
                                                    String description, ZonedDateTime deadline,
                                                    Status status) {
    Task taskForEdit = taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException(taskId));
    Task newTask = new Task();
    newTask.setCreationDate(taskForEdit.getCreationDate());
    newTask.setId(taskForEdit.getId());
    newTask.setAuthorId(taskForEdit.getAuthorId());
    if (name != null) {
      newTask.setName(name);
    } else {
      newTask.setName(taskForEdit.getName());
    }
    if (performerId != null) {
      newTask.setPerformerId(performerId);
    } else {
      newTask.setPerformerId(taskForEdit.getPerformerId());
    }
    if (description != null) {
      newTask.setDescription(description);
    } else {
      newTask.setDescription(taskForEdit.getDescription());
    }
    if (deadline != null) {
      newTask.setDeadline(deadline);
    } else {
      newTask.setDeadline(taskForEdit.getDeadline());
    }
    if (status != null) {
      newTask.setStatus(status);
    } else {
      newTask.setStatus(taskForEdit.getStatus());
    }
    taskRepository.save(newTask);
    return Optional.of(new TaskResponse(newTask.getId()));
  }

  /**
   * Edits a task by id if you're either an author or performer of the task.
   *
   * @param taskId      id of a task for editing
   * @param name        new task name
   * @param description new task description
   * @param status      new task status
   * @param userId      id of user who makes this request
   * @return id of the edited task.
   */
  @Transactional
  public Optional<TaskResponse> editTaskByUser(Long taskId, String name, String description,
                                               Status status, Long userId) {
    Task taskForEdit = taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException(taskId));
    if (!(taskForEdit.getAuthorId().equals(userId)
        || (taskForEdit.getPerformerId().equals(userId)))) {
      return Optional.empty();
    }
    if (name == null && description == null && status == null) {
      return Optional.empty();
    }
    Task newTask = new Task();
    newTask.setCreationDate(taskForEdit.getCreationDate());
    newTask.setId(taskForEdit.getId());
    newTask.setAuthorId(taskForEdit.getAuthorId());
    if (name != null) {
      newTask.setName(name);
    } else {
      newTask.setName(taskForEdit.getName());
    }
    if (description != null) {
      newTask.setDescription(description);
    } else {
      newTask.setDescription(taskForEdit.getDescription());
    }
    if (status != null) {
      newTask.setStatus(status);
    } else {
      newTask.setStatus(taskForEdit.getStatus());
    }
    if (taskForEdit.getPerformerId() != null) {
      newTask.setPerformerId(taskForEdit.getPerformerId());
    }
    if (taskForEdit.getDeadline() != null) {
      newTask.setDeadline(taskForEdit.getDeadline());
    }
    taskRepository.save(newTask);
    return Optional.of(new TaskResponse(newTask.getId()));
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
              "Status: " + status.name() + ", tasks: " + taskRepository.findByStatus(status)
              .stream().count() + "\n"
      });
    }
    statisticsCollector.csvOutput(dataLines);
  }

  @Transactional
  public Optional<TaskResponse> deleteTaskByUser(Long id, Long userId) {
    Task taskForDelete = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));
    if (taskForDelete.getAuthorId().equals(userId)) {
      taskRepository.delete(taskForDelete);
      return Optional.of(new TaskResponse(id));
    } else {
      return Optional.empty();
    }
  }

  @Transactional
  public Optional<TaskResponse> deleteTaskByModerator(Long id) {
    Task taskForDelete = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));
    taskRepository.delete(taskForDelete);
    return Optional.of(new TaskResponse(id));
  }
}
