package org.study.tracker.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response<T> {
  private ResponseType type;
  private T payload;
}
