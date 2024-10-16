package org.study.tracker.service;

import java.util.Optional;
import lombok.NoArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.study.tracker.Role;
import org.study.tracker.model.User;
import org.study.tracker.repository.UserRepository;
import org.study.tracker.responses.UserResponse;

@SpringBootTest
@NoArgsConstructor
public class UserServiceTest {
  private final User userForSave = new User(null, "testName",
      "Testpassword123", Role.ROLE_USER);
  private final User testAdmin = new User(null, "testAdmin", "testAdmin",
      Role.ROLE_ADMIN);

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void saveTest() {
    User savedUser = userRepository.save(userForSave);
    assertEquals(userForSave, savedUser);
    userRepository.delete(userForSave);
  }

  /**
   * Check that the user is saved into the DB
   * and that you can't create new user with the same username
   */
  @Test
  public void createTest() {
    UserResponse createdUser = userService.create(userForSave);
    assertEquals(userForSave.getId(), createdUser.getId());
    assertThrows(RuntimeException.class,
        () -> userService.create(userForSave));
    userRepository.delete(userForSave);
  }

  @Test
  public void getByUsernameTest() {
    assertThrows(UsernameNotFoundException.class,
        () -> userService.getByUsername(userForSave.getUsername()));
    UserResponse foundUser = userService.create(userForSave);
    assertEquals(userForSave.getId(), foundUser.getId());
    userRepository.delete(userForSave);
  }

  @WithMockUser(username = "spring")
  @Test
  public void loadUserByUsernameTest() {
    userRepository.save(userForSave);
    assertThat(userService.loadUserByUsername("TestName")).isNotNull();
    userRepository.delete(userForSave);
  }

  @WithMockUser(username = "testAdmin")
  @Test
  public void editUserRoleTest() {
    userRepository.save(testAdmin);
    userRepository.save(userForSave);
    Optional<User> user = userRepository.findByUsername("TestName");
    assertThat(user.get().getRole().equals(Role.ROLE_USER));
    userService.editUserRole(user.get().getId(), Role.ROLE_GROUP_MODERATOR);
    assertThat(user.get().getRole().equals(Role.ROLE_GROUP_MODERATOR));
    userRepository.delete(userForSave);
  }

  @WithMockUser(username = "testAdmin")
  @Test
  public void deleteUserTest() {
    userRepository.save(testAdmin);
    userRepository.save(userForSave);
    Optional<User> user = userRepository.findByUsername("TestName");
    assertThat(user.get()).isNotNull();
    userService.deleteUser(user.get().getId());
    assertThrows(UsernameNotFoundException.class,
        () -> userService.getByUsername(user.get().getUsername()));
    userRepository.delete(testAdmin);
  }
}
