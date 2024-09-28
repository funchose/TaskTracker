package org.study.tracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.study.tracker.TaskTrackerApplication;
import org.study.tracker.repository.TaskRepository;

@SpringBootTest(classes = TaskTrackerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskServiceTest {
  @MockBean
  TaskRepository taskRepository;

  @Autowired
  public TestRestTemplate template = new TestRestTemplate();

  @Test
  public void getTasksTest() {
//    final <Task> response = this.template.exchange("/tasks", HttpMethod.GET, null,
//        new ParameterizedTypeReference<>() {});
//    assertThat(HttpStatus.resolve(response.getStatusCode().value())).isEqualTo(HttpStatus.OK);
  }
}
