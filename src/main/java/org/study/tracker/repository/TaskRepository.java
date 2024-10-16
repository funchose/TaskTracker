package org.study.tracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.tracker.Status;
import org.study.tracker.model.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Optional<Task>> findByStatus(Status status);
}
