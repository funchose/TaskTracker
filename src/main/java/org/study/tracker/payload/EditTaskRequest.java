package org.study.tracker.payload;

import jakarta.persistence.Column;
import org.study.tracker.Status;

import java.time.ZonedDateTime;

public class EditTaskRequest {

  @Column(name = "performer_id")
  private Long performerId;

  private String name;

  private String description;
  private Status status;
  private ZonedDateTime deadline;

  public Long getPerformerId() {
    return performerId;
  }

  public void setPerformerId(Long performerId) {
    this.performerId = performerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public ZonedDateTime getDeadline() {
    return deadline;
  }

  public void setDeadline(ZonedDateTime deadline) {
    this.deadline = deadline;
  }
}
