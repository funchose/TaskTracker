package org.study.tracker.payload;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.study.tracker.Status;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
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
}
