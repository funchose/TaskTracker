package org.study.tracker.controller;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.model.User;
import org.study.tracker.payload.EditUserRoleRequest;
import org.study.tracker.responses.UserResponse;
import org.study.tracker.security.jwt.JwtService;
import org.study.tracker.service.UserService;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final AuthenticationManager authenticationManager;

  private final UserService userService;

  private final JwtService jwtService;

  @Transactional
  @GetMapping("/admin/users")
  public List<UserResponse> getUsers() {
    return userService.getUsers();
  }

  @Transactional
  @DeleteMapping("/admin/users/{id}")
  public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id,
                                                 @AuthenticationPrincipal User user) {
    var response = new UserResponse(id);
    if (id.equals(user.getId())) {
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    } else {
      userService.deleteUser(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
  }

  @Transactional
  @PutMapping("/admin/users/{id}")
  public ResponseEntity<UserResponse> editUserRole(@PathVariable Long id,
                                                   @RequestBody EditUserRoleRequest request) {
    UserResponse response = userService.editUserRole(id, request.getRole());
    if (!(response.getId() == null)) {
      logger.info("Role of user with ID " + id + " was changed to " + request.getRole());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
  }
}
