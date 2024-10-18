package org.study.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Operations with users")
@Getter
@Setter
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final AuthenticationManager authenticationManager;

  private final UserService userService;

  private final JwtService jwtService;

  @Transactional
  @Operation(summary = "Get list of all users",
      description = "Returns a list of all users")
  @GetMapping("/admin/users")
  public List<UserResponse> getUsers() {
    return userService.getUsers();
  }

  /**
   * Deletes a user by ID if current user has role ROLE_ADMIN. He can't delete himself and
   * other users if they have tasks.
   *
   * @param id - ID of user to be deleted
   * @param user - current user
   * @return ID of deleted user
   */
  @Transactional
  @Operation(summary = "Delete a user",
      description = "Deletes a user with required ID"
          + " if he doesn't have any tasks as author or performer")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User is deleted"),
      @ApiResponse(responseCode = "404", description = "User is not found"),
      @ApiResponse(responseCode = "409", description = "Admin cannot delete himself "
          + " or other users with existing tasks")
  })
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

  /**
   * Edits user role.
   *
   * @param id - ID of user whose role must be edited
   * @param request - new Role
   * @return - ID of user with edited Role
   */
  @Transactional
  @Operation(summary = "Edit user role",
      description = "Edits a role of any user but not the admin himself")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User role is edited"),
      @ApiResponse(responseCode = "404", description = "User is not found"),
      @ApiResponse(responseCode = "409", description = "Admin cannot edit his own role")
  })
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
