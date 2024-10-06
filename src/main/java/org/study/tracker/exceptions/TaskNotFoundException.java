package org.study.tracker.exceptions;

/**
 * Custom exception for a case when a task is not found in the DB
 */
public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(Long id) {
    super("Task not found with ID: " + id);
  }
}
