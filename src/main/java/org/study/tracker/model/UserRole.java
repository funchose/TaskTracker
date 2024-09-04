package org.study.tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.study.tracker.utils.UserRoleKey;

@Entity
@Table(name = "user_roles")
public class UserRole {
  @Id
  UserRoleKey userRoleKey;

  public Long getUserId() {
    return userRoleKey.getUserId();
  }

  public void setUserId(Long userId) {
    this.setUserId(userId);
  }

  public String getRoleName() {
    return userRoleKey.getRoleName();
  }

  public void setRoleName(String roleName) {
    this.setRoleName(roleName);
  }

  public UserRole() {
  }
}
