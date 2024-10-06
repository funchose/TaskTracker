package org.study.tracker.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TaskResponse {
  private Long id;

  public TaskResponse(Long id) {
    setId(id);
  }

}
