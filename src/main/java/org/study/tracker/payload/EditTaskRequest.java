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
  private Status status;
  private ZonedDateTime deadline;
}
