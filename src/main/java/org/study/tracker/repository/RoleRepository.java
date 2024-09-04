package org.study.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.tracker.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
