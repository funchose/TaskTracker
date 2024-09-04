package org.study.tracker.model;

import jakarta.persistence.*;
import org.study.tracker.Status;
import org.study.tracker.utils.StatusConverter;

import java.util.Date;

@Entity
@Table(name = "tasks")
@Convert(attributeName = "status", converter = StatusConverter.class)
public class Task {

  private String name;

  private String description;

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  private Date deadline;

  @Enumerated(EnumType.STRING)
  @Column
  private Status status;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private Long id;
  @Column(name = "author_id")
  private Long authorId;
  @Column(name = "performer_id")
  private Long performerId;

  public Task() {
  }

  public Task(Long id, Long authorId, String name, String description, Long performerId, Status status) {
    this.id = id;
    this.authorId = authorId;
    this.name = name;
    this.description = description;
    this.performerId = performerId;
    this.status = Status.OPEN;
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

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public Long getId() {
    return id;
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
}
