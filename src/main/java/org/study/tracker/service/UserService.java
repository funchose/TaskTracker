package org.study.tracker.service;

import jakarta.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.tracker.Role;
import org.study.tracker.exceptions.UserNotFoundException;
import org.study.tracker.model.User;
import org.study.tracker.repository.UserRepository;
import org.study.tracker.responses.TaskResponse;
import org.study.tracker.responses.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final TaskService taskService;

  /**
   * Method collects information about users: id, username, amount of tasks to perform, amount of
   * tasks with this user as an author.
   *
   * @return List of User Responses
   */
  @Transactional
  public List<UserResponse> getUsers() {
    ArrayList<UserResponse> userResponseList = new ArrayList<>();
    List<User> users = userRepository.findAll();
    for (User user : users) {
      UserResponse userResponse = new UserResponse();
      userResponse.setId(user.getId());
      userResponse.setUsername(user.getUsername());
      userResponse.setRole(user.getRole());
      List<TaskResponse> taskResponseList;
      taskResponseList = taskService.getTasks();
      userResponse.setTasksToDoAmount(taskResponseList.stream()
          .filter(taskResponse -> taskResponse.getPerformerId().equals(user.getId())).count());
      userResponse.setUserTasksAmount(taskResponseList.stream()
          .filter(taskResponse -> taskResponse.getAuthorId().equals(user.getId())).count());
      userResponseList.add(userResponse);
    }
    logger.debug("List of users is received");
    return userResponseList;
  }

  /**
   * Saves the user in the DB without checking his presence.
   *
   * @param user - user that must be saved in the DB
   * @return id of saved user
   */
  public UserResponse save(User user) {
    userRepository.save(user);
    var userResponse = new UserResponse();
    userResponse.setId(user.getId());
    return userResponse;
  }

  /**
   * Saves user in the DB with preliminary check of his presence.
   *
   * @param user - user that must be saved in the DB
   * @return id of saved user
   */
  public UserResponse create(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new UnexpectedTypeException("This user already exists");
    }
    return save(user);
  }

  public User getByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
  }

  public UserDetailsService userDetailsService() {
    return this::getByUsername;
  }

  public User getCurrentUser() {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    return getByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return getByUsername(username);
  }

  /**
   * Deletes the user from DB by ID.
   *
   * @param userId - ID of user to be deleted from DB
   */
  @Transactional
  public void deleteUser(Long userId) {
    User userForDelete = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    userRepository.delete(userForDelete);
    logger.info("User with ID " + userId + " was deleted");
  }

  /**
   * Edits role of user with ID.
   *
   * @param id - ID of user whose role must be edited
   * @param role - new Role
   * @return ID of user with edited role
   */
  @Transactional
  public UserResponse editUserRole(Long id, Role role) {
    User userToEdit = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    if (!userToEdit.getRole().equals(Role.ROLE_ADMIN)) {
      User user = new User();
      user.setId(userToEdit.getId());
      user.setUsername(userToEdit.getUsername());
      user.setPassword(userToEdit.getPassword());
      user.setRole(role);
      userRepository.save(user);
      return new UserResponse(id);
    } else {
      return new UserResponse();
    }
  }
}
