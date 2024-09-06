package org.study.tracker.payload;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.study.tracker.Status;

import java.time.ZonedDateTime;

public class AddTaskRequest {
  @NotNull
  @Column(name = "author_id")
  private Long authorId;
  @Column(name = "performer_id")
  private Long performerId;
  @NotNull(message = "Task name cannot be null")
  @NotEmpty(message = "Task name cannot be empty")
  private String name;
  @NotNull(message = "Task description cannot be null")
  @NotEmpty(message = "Task description cannot be empty")
  private String description;
  private Status status;
  private ZonedDateTime deadline;

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public void setPerformerId(Long performerId) {
    this.performerId = performerId;
  }

  public Long getPerformerId() {
    return performerId;
  }

  public void setDeadline(ZonedDateTime deadline) {
    this.deadline = deadline;
  }

  public ZonedDateTime getDeadline() {
    return deadline;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public AddTaskRequest() {
  }
}
