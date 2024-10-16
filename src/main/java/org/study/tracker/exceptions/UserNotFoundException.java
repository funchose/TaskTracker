package org.study.tracker.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long id) {
    super("User is not found with ID " + id);
  }
}
