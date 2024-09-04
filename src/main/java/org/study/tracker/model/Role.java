package org.study.tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "roles")
public class Role {

  public void setName(String name) {
    this.name = name;
  }

  @Id
  @Column(length = 50)
  private String name;

  private String description;

  public Role() {
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Role(String name) {
    this.name = name;
  }
}
