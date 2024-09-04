package org.study.tracker.payload;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.study.tracker.Status;

public class AddTaskRequest {
  @NotNull
  @Column(name = "author_id")
  private Long authorId;
  @NotNull(message = "Task name cannot be null")
  @NotEmpty(message = "Task name cannot be empty")
  private String name;
  @NotNull(message = "Task description cannot be null")
  @NotEmpty(message = "Task description cannot be empty")
  private String description;
  private Status status;

  public void setAuthorId(String authorId) {
    this.authorId = Long.valueOf(authorId);
  }

  public void setName(String name) {
    this.name = name;
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

  public Long getAuthorId() {
    return authorId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public AddTaskRequest() {
  }
}
