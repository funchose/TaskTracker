package org.study.tracker.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Delete task request")
public class DeleteTaskRequest {
  private Long id;
}
