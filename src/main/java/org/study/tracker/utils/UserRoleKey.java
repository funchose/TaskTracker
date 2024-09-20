//package org.study.tracker.utils;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Embeddable
//@Getter
//@Setter
//@NoArgsConstructor
//public class UserRoleKey implements Serializable {
//
//  @Column(name = "user_id")
//  private Long userId;
//
//  @Column(name = "role_name")
//  private String roleName;
//
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    UserRoleKey key = (UserRoleKey) o;
//    return roleName == key.roleName &&
//        userId == key.userId;
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hash(roleName, userId);
//  }
//}
