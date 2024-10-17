package org.study.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.tracker.model.User;

import java.util.Optional;

/**
 * User repository class.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds a user by username.
   *
   * @param username - username in DB
   * @return Optional of User object
   */
  Optional<User> findByUsername(String username);

  /**
   * Checks if the user with username exists.
   *
   * @param username - username in DB
   * @return true if user with this username exists, false if not
   */
  boolean existsByUsername(String username);
}
