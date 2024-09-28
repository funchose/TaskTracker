package org.study.tracker.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.tracker.Status;
import org.study.tracker.model.Task;
import org.study.tracker.repository.TaskRepository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@NoArgsConstructor
@Getter
@Component
public class StatisticsCollector {
  @Autowired
  private TaskRepository taskRepository;
  int tasksTotal;
  HashSet<Status> statusSet;

  public void getStatuses() {
    statusSet = new HashSet<>();
    for (Task task : taskRepository.findAll()) {
      statusSet.add(task.getStatus());
    }
  }

  public String convertToCSV(String[] data) {
    return Stream.of(data)
        .collect(Collectors.joining(","));
  }

  public void csvOutput(List<String[]> dataLines) throws IOException {
    File csvOutputFile = new File("src/main/resources/Statistics.csv");
    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
      dataLines.stream()
          .map(this::convertToCSV)
          .forEach(pw::println);
    }
  }
}
