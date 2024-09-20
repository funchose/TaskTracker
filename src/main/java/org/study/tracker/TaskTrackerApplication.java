package org.study.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaRepositories
@ControllerAdvice(annotations = RestController.class)
public class TaskTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TaskTrackerApplication.class, args);
  }
}
