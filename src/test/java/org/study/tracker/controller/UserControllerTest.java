package org.study.tracker.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.study.tracker.Role;
import org.study.tracker.TaskTrackerApplication;
import org.study.tracker.model.User;
import org.study.tracker.responses.UserResponse;
import org.study.tracker.service.UserService;

/**
 * Test of user controller functionality.
 */
@SpringBootTest(classes = TaskTrackerApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerTest {

  User testAdmin = new User(null, "testAdmin", "testAdmin",
      Role.ROLE_ADMIN);

  @Autowired
  UserController userController;

  @Autowired
  UserService userService;

  @WithMockUser(username = "testAdmin")
  @Test
  public void deleteAdminTest() {
    userService.save(testAdmin);
    User user = userService.getByUsername("testAdmin");
    ResponseEntity<UserResponse> entity = userController.deleteUser(user.getId(), testAdmin);
    assertThat(entity.getStatusCode().value()).isEqualTo(HttpStatus.CONFLICT.value());
    userService.deleteUser(user.getId());
  }
}
