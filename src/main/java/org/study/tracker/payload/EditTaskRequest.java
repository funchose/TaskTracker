package org.study.tracker.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.study.tracker.Status;

import java.time.ZonedDateTime;

@Data
@Schema(description = "Edit task request")
public class EditTaskRequest {
  private Long performerId;
  private String name;
  private String description;
  @Schema(description = "Status", example = "OPEN, IN_PROGRESS, DONE, UNDER_REVIEW,"
      + "REJECTED, REOPENED, CLOSED")
  private Status status;
  private ZonedDateTime deadline;
}
