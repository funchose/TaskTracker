package org.study.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.tracker.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
