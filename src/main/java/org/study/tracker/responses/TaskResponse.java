package org.study.tracker.responses;

import org.study.tracker.model.Task;

public class TaskResponse extends Task {
  public TaskResponse(Long id) {
    setId(id);
  }
}
