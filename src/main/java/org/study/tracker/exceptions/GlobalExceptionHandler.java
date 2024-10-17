package org.study.tracker.exceptions;

import jakarta.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handles some exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status,
                                                                WebRequest request) {
    Map<String, List<String>> body = new HashMap<>();
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
    body.put("errors", errors);
    logger.debug("Some arguments are not valid: " + errors);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFoundException(
      TaskNotFoundException exception) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
        exception.getMessage());
    logger.debug("Task is not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse>
  handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(),
        exception.getMessage()
        + ". Before deleting the user, delete all his tasks "
            + "or change the task author or performer");
    logger.debug("User tried to delete himself or another user with existing tasks");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse>
  handleUserNotFoundException(UserNotFoundException exception) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
        exception.getMessage());
    logger.debug("User is not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  public ResponseEntity<ErrorResponse>
  handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        exception.getMessage());
    logger.debug("Data is invalid");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(UnexpectedTypeException.class)
  public ResponseEntity<ErrorResponse>
  handleUnexpectedTypeException(UnexpectedTypeException exception) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        "This username is already taken");
    logger.debug(exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse>
  handleNoSuchElementException(NoSuchElementException exception) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
        "Item doesn't exist");
    logger.debug(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }
}

