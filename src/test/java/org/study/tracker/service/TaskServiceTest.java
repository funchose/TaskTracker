package org.study.tracker.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.study.tracker.TaskTrackerApplication;
import org.study.tracker.repository.TaskRepository;

@SpringBootTest(classes = TaskTrackerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskServiceTest {
  @MockBean
  TaskRepository taskRepository;

  @Autowired
  public TestRestTemplate template = new TestRestTemplate();

  @Test
  @PreAuthorize("hasRole('ADMIN')")
  // @WithUserDetails(/*username = "admin", password = "admin", roles = "ADMIN", */value = "user21", userDetailsServiceBeanName = "userService")
  public void getTasksTest() {
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    ResponseEntity<Object> response = this.template.exchange("/tasks", HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {
        });
    System.out.println(HttpStatus.resolve(response.getStatusCode().value())); //.isEqualTo(HttpStatus.OK)

  }
}
