package org.study.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.study.tracker.Status;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "author_id")
  private Long authorId;
  @Column(name = "performer_id")
  private Long performerId;
  @NotBlank(message = "Task name is required")
  @Column
  private String name;
  @NotBlank(message = "Task description is required")
  @Column
  private String description;
  @Column
  private ZonedDateTime deadline;
  @NotNull
  @Column(name = "creation_date")
  private ZonedDateTime creationDate;
  @Enumerated(EnumType.STRING)
  @Column
  private Status status;


  public Task(Long id, Long authorId, String name, String description, ZonedDateTime deadline, Long performerId) {
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.status = Status.OPEN;
    this.id = id;
    this.authorId = authorId;
    this.performerId = performerId;
    this.creationDate = ZonedDateTime.now();
  }
}

