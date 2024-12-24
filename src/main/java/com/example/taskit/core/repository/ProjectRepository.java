package com.example.taskit.core.repository;

import com.example.taskit.core.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByMembersId(Long userId);
    List<Project> findByCreatedById(Long userId);
}
