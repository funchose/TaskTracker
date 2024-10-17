package org.study.tracker.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.study.tracker.Status;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Add task request")
public class AddTaskRequest {
  @NotNull
  private Long authorId;
  private Long performerId;
  @NotNull(message = "Task name cannot be null")
  @NotEmpty(message = "Task name cannot be empty")
  private String name;
  @NotNull(message = "Task description cannot be null")
  @NotEmpty(message = "Task description cannot be empty")
  private String description;
  @Schema(description = "Status", example = "OPEN, IN_PROGRESS, DONE, UNDER_REVIEW,"
      + "REJECTED, REOPENED, CLOSED")
  private Status status;
  private ZonedDateTime deadline;
}
