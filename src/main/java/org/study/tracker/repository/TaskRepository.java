package org.study.tracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.tracker.Status;
import org.study.tracker.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Optional<Task>> findByStatus(Status status);

  Task findByName(String name);
}
