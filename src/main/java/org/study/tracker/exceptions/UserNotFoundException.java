package org.study.tracker.exceptions;

/**
 * Custom exception for a case when a user is not found in the DB.
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long id) {
    super("User is not found with ID " + id);
  }
}
