package org.study.tracker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Handles the error responses
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  private int statusCode;
  private String message;

  public ErrorResponse(String message) {
    super();
    this.message = message;
  }
}
