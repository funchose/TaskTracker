package org.study.tracker.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.study.tracker.Role;
import org.study.tracker.Status;
import org.study.tracker.TaskTrackerApplication;
import org.study.tracker.model.User;
import org.study.tracker.payload.AddTaskRequest;
import org.study.tracker.repository.TaskRepository;
import org.study.tracker.repository.UserRepository;
import org.study.tracker.responses.UserResponse;

/**
 * Test of task service functionality.
 */
@SpringBootTest(classes = TaskTrackerApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskServiceTest {
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskService taskService;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  private final User userForSave = new User(null, "testName",
      "Testpassword123", Role.ROLE_USER);
  private final User testModerator = new User(null, "testModerator",
      "testModerator", Role.ROLE_GROUP_MODERATOR);

  @Test
  @WithMockUser(username = "testName")
  public void getUserTasksToDoTest() {
    User user = userRepository.save(userForSave);
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

    assertThat(taskService.getUserTasksToDo(user.getId()).size()).isEqualTo(3);
    taskRepository.delete(taskRepository.findByName(request1.getName()));
    taskRepository.delete(taskRepository.findByName(request2.getName()));
    taskRepository.delete(taskRepository.findByName(request3.getName()));
    userRepository.delete(user);
  }

  @Test
  @WithMockUser(username = "testName")
  public void editTaskByUserTest() {
    User user = userRepository.save(userForSave);
    AddTaskRequest request1 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    taskService.createTask(request1, user.getId());
    Long taskId = taskRepository.findByName(request1.getName()).getId();
    var taskResponse = taskService.editTaskByUser(taskId,
        "new name1", "new description1", Status.CLOSED, user.getId());
    assertThat(taskRepository.findById(taskId).get().getName())
        .isEqualTo("new name1");
    assertThat(taskRepository.findById(taskId).get().getDescription())
        .isEqualTo("new description1");
    assertThat(taskRepository.findById(taskId).get().getStatus())
        .isEqualTo(Status.CLOSED);
    taskRepository.deleteById(taskId);
    userRepository.delete(user);
  }

  @Test
  @WithMockUser(username = "testModerator")
  public void editTaskByModeratorTest() {
    User user = userRepository.save(testModerator);
    AddTaskRequest request1 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    taskService.createTask(request1, user.getId());
    Long taskId = taskRepository.findByName(request1.getName()).getId();
    var taskResponse = taskService.editTaskByModerator(taskId,
        null, null, null,
        ZonedDateTime.now().plusDays(5L), null);
    assertThat(taskRepository.findById(taskId).get().getDeadline().getDayOfMonth())
        .isEqualTo(ZonedDateTime.now().plusDays(5L).getDayOfMonth());
    taskRepository.deleteById(taskId);
    userRepository.delete(user);
  }

  @Test
  @WithMockUser(username = "testName")
  public void deleteTaskByUserTest() {
    UserResponse user = userService.create(userForSave);
    AddTaskRequest request1 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    taskService.createTask(request1, user.getId());
    Long taskId = taskRepository.findByName(request1.getName()).getId();
    taskService.deleteTaskByUser(taskId, user.getId());
    assertThat(taskRepository.findById(taskId)).isEqualTo(Optional.empty());
    taskRepository.deleteById(taskId);
    userRepository.deleteById(user.getId());
  }

  @Test
  @WithMockUser(username = "testName")
  public void deleteTaskByModeratorTest() {
    UserResponse user = userService.create(userForSave);
    UserResponse moderator = userService.create(testModerator);
    AddTaskRequest request1 = new AddTaskRequest();
    request1.setName("name1");
    request1.setDescription("description1");
    taskService.createTask(request1, user.getId());
    Long taskId = taskRepository.findByName(request1.getName()).getId();
    taskService.deleteTaskByModerator(taskId);
    assertThat(taskRepository.findById(taskId)).isEqualTo(Optional.empty());
    taskRepository.deleteById(taskId);
    userRepository.deleteById(user.getId());
    userRepository.deleteById(moderator.getId());
  }
}
