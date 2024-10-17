package org.study.tracker.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.study.tracker.Role;
import org.study.tracker.Status;
import org.study.tracker.TaskTrackerApplication;
import org.study.tracker.model.User;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.payload.EditTaskRequest;
import org.study.tracker.repository.TaskRepository;
import org.study.tracker.repository.UserRepository;
import org.study.tracker.responses.TaskResponse;
import org.study.tracker.responses.UserResponse;
import org.study.tracker.service.TaskService;
import org.study.tracker.service.UserService;

/**
 * Test of task controller functionality.
 */
@SpringBootTest(classes = TaskTrackerApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskControllerTest {
  @Autowired
  TaskService taskService;

  @Autowired
  UserService userService;
  @Autowired
  TaskController taskController;
  @Autowired
  TaskRepository taskRepository;

  @Autowired
  UserRepository userRepository;

  private final User userForSave = new User(null, "testName",
      "Testpassword123", Role.ROLE_USER);
  private final User testModerator = new User(null, "testModerator",
      "testModerator", Role.ROLE_GROUP_MODERATOR);

  @Test
  @WithMockUser(username = "testName")
  public void getUserTasksToDoTest() {
    UserResponse user = userService.create(userForSave);
    AddTaskRequest request1 = new AddTaskRequest();
    AddTaskRequest request2 = new AddTaskRequest();
    AddTaskRequest request3 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    request2.setName("name2");
    request2.setDescription("description2");
    request3.setName("name3");
    request3.setDescription("description3");

    taskService.createTask(request1, user.getId());
    taskService.createTask(request2, user.getId());
    taskService.createTask(request3, user.getId());

    assertThat(taskController.getUserTasksToDo(userForSave).size()).isEqualTo(3);
    taskRepository.delete(taskRepository.findByName(request1.getName()));
    taskRepository.delete(taskRepository.findByName(request2.getName()));
    taskRepository.delete(taskRepository.findByName(request3.getName()));
    userRepository.delete(userForSave);
  }

  @Test
  @WithMockUser(username = "testName")
  public void editTaskByUserTest() {
    User user = userRepository.save(userForSave);
    AddTaskRequest addRequest = new AddTaskRequest();
    addRequest.setName("name1");
    addRequest.setDescription("description1");
    taskController.createTask(addRequest, user);
    EditTaskRequest editRequest = new EditTaskRequest();
    editRequest.setName("editedName");
    editRequest.setDescription("editedDescription");
    editRequest.setStatus(Status.CLOSED);
    Long taskId = taskRepository.findByName(addRequest.getName()).getId();
    taskController.editTask(taskId, editRequest, user);
    assertThat(taskRepository.findById(taskId).get().getName())
        .isEqualTo(editRequest.getName());
    assertThat(taskRepository.findById(taskId).get().getDescription())
        .isEqualTo(editRequest.getDescription());
    assertThat(taskRepository.findById(taskId).get().getStatus())
        .isEqualTo(Status.CLOSED);
    taskRepository.deleteById(taskId);
    userRepository.delete(user);
  }

  @Test
  @WithMockUser(username = "testModerator")
  public void editTaskByModeratorTest() {
    User user = userRepository.save(testModerator);
    AddTaskRequest addRequest = new AddTaskRequest();
    addRequest.setName("name1");
    addRequest.setDescription("description1");
    addRequest.setDeadline(ZonedDateTime.now());
    taskController.createTask(addRequest, user);
    EditTaskRequest editRequest = new EditTaskRequest();
    editRequest.setName("editedName");
    editRequest.setDescription("editedDescription");
    editRequest.setDeadline(ZonedDateTime.now().plusDays(5L));
    Long taskId = taskRepository.findByName(addRequest.getName()).getId();
    taskController.editTask(taskId, editRequest, user);
    assertThat(taskRepository.findById(taskId).get().getName())
        .isEqualTo(editRequest.getName());
    assertThat(taskRepository.findById(taskId).get().getDescription())
        .isEqualTo(editRequest.getDescription());
    assertThat(taskRepository.findById(taskId).get().getDeadline().getDayOfMonth())
        .isEqualTo(ZonedDateTime.now().plusDays(5L).getDayOfMonth());
    taskRepository.deleteById(taskId);
    userRepository.delete(user);
  }

  @Test
  @WithMockUser(username = "testName")
  public void deleteTaskByUser1Test() {
    User user = userRepository.save(userForSave);
    AddTaskRequest request1 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    taskController.createTask(request1, user);
    Long taskId = taskRepository.findByName(request1.getName()).getId();
    taskController.deleteTask(taskId, user);
    assertThat(taskRepository.findById(taskId)).isEqualTo(Optional.empty());
    taskRepository.deleteById(taskId);
    userRepository.deleteById(user.getId());
  }

  @Test
  @WithMockUser(username = "testName")
  public void deleteTaskByUser2Test() {
    User user = userRepository.save(userForSave);
    User moderator = userRepository.save(testModerator);
    AddTaskRequest request1 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    taskService.createTask(request1, moderator.getId());
    Long taskId = taskRepository.findByName(request1.getName()).getId();
    ResponseEntity<TaskResponse> entity = taskController.deleteTask(taskId, user);
    assertThat(entity.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    taskRepository.deleteById(taskId);
    userRepository.deleteById(user.getId());
    userRepository.deleteById(moderator.getId());
  }

  @Test
  @WithMockUser(username = "testModerator")
  public void deleteTaskByModeratorTest() {
    User user = userRepository.save(userForSave);
    User moderator = userRepository.save(testModerator);
    AddTaskRequest request1 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    taskService.createTask(request1, user.getId());
    Long taskId = taskRepository.findByName(request1.getName()).getId();
    taskController.deleteTask(taskId, moderator);
    assertThat(taskRepository.findById(taskId)).isEqualTo(Optional.empty());
    taskRepository.deleteById(taskId);
    userRepository.deleteById(user.getId());
    userRepository.deleteById(moderator.getId());
  }
}
