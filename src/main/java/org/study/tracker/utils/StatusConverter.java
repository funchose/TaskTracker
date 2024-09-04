package org.study.tracker.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.study.tracker.Status;

@Converter
public class StatusConverter implements AttributeConverter<Status, String> {
  @Override
  public String convertToDatabaseColumn(Status status) {
    return status.toString();
  }

  @Override
  public Status convertToEntityAttribute(String status) {
    return Status.valueOf(status);
  }
}
