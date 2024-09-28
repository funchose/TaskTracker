package org.study.tracker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
  ROLE_USER("USER", "task creation, editing, changing task status"),
  ROLE_GROUP_MODERATOR("ROLE_GROUP_MODERATOR",
      "user rights + changing the deadline, setting a performer, task deleting"),
  ROLE_PROJECT_MANAGER("ROLE_PROJECT_MANAGER", "tasks statistics collection"),
  ROLE_ADMIN("ROLE_ADMIN", "all functions");
  private final String name;
  private final String description;
}
