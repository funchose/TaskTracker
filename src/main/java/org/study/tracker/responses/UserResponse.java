package org.study.tracker.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.study.tracker.Role;

@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
  private Long id;
  private String username;
  private Role role;
  private Long tasksToDoAmount;
  private Long userTasksAmount;

  public UserResponse(Long id) {
    this.id = id;
  }
}
