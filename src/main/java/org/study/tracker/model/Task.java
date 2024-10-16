package org.study.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.study.tracker.Status;

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


  /**
   * Creates a task with status OPEN and creation date equal the current moment of time.
   *
   * @param id          the task id
   * @param authorId    the task author id (by default - the task creator, can't be changed)
   * @param name        short name of the task
   * @param description full name of the task or full description what must be done
   * @param deadline    the task due date
   * @param performerId person who will perform the task
   */
  public Task(Long id, Long authorId, String name, String description, ZonedDateTime deadline,
              Long performerId) {
    this.name = name;
    this.description = description;
    this.deadline = deadline;
    this.status = Status.OPEN;
    this.id = id;
    this.authorId = authorId;
    if (performerId == null) {
      this.performerId = authorId;
    }
    else {
      this.performerId = performerId;
    }
    this.creationDate = ZonedDateTime.now();
  }
}

