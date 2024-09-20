package org.study.tracker;

public enum Status {
  OPEN("OPEN"),
  IN_PROGRESS("IN_PROGRESS"),
  RESOLVED("RESOLVED"),
  REOPENED("REOPENED"),
  CLOSED("CLOSED");
  private final String status;

  Status(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return this.status;
  }
}
