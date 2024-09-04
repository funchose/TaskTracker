package org.study.tracker.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoleKey implements Serializable {
  public UserRoleKey() {
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "role_name")
  private String roleName;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserRoleKey key = (UserRoleKey) o;
    return roleName == key.roleName &&
        userId == key.userId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleName, userId);
  }
}
