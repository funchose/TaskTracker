package org.study.tracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.tracker.Status;
import org.study.tracker.model.Task;

/**
 * Task repository class.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  /**
   * Finds all tasks with required status.
   *
   * @param status - task status
   * @return list of tasks with required status
   */
  List<Optional<Task>> findByStatus(Status status);

  /**
   * Finds a task by name.
   *
   * @param name - task name in DB
   * @return Task object
   */
  Task findByName(String name);
}
