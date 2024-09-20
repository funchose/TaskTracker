package org.study.tracker.payload;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.study.tracker.Status;

import java.time.ZonedDateTime;

@Getter
@Setter
public class EditTaskRequest {

  @Column(name = "performer_id")
  private Long performerId;
  private String name;
  private String description;
  private Status status;
  private ZonedDateTime deadline;
}
