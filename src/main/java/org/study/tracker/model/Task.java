package org.study.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.study.tracker.Status;
import org.study.tracker.utils.StatusConverter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "tasks")
@Convert(attributeName = "status", converter = StatusConverter.class)
@Getter
@Setter
@NoArgsConstructor
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
}

