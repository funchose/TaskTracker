package org.study.tracker.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TaskResponse {

  private Long id;
  private String name;
  private Long authorId;
  private Long performerId;

  public TaskResponse(Long id) {
    setId(id);
  }
}
