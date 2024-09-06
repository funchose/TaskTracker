package org.study.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.study.tracker.Status;
import org.study.tracker.utils.StatusConverter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "tasks")
@Convert(attributeName = "status", converter = StatusConverter.class)
public class Task {

  private String name;

  private String description;

  private ZonedDateTime deadline;

  @NotNull
  private ZonedDateTime creationDate;
  @Enumerated(EnumType.STRING)
  @Column
  private Status status;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private Long taskId;
  @Column(name = "author_id")
  private Long authorId;
  @Column(name = "performer_id")
  private Long performerId;

  public Task() {
  }

  public Task(Long taskId, Long authorId, String name, String description, ZonedDateTime deadline, Status status, Long performerId) {
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.status = status;
    this.taskId = taskId;
    this.authorId = authorId;
    this.performerId = performerId;
    this.creationDate = ZonedDateTime.now();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
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

  public Long getId() {
    return taskId;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public Long getPerformerId() {
    return performerId;
  }

  public void setPerformerId(Long performerId) {
    this.performerId = performerId;
  }

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) {
    this.creationDate = creationDate;
  }
}

