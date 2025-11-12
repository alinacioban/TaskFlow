package com.example.taskhub.repo;

import com.example.taskhub.domain.Task;
import com.example.taskhub.domain.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    List<Task> findByProjectId(Long projectId, Sort sort);

    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);

    List<Task> findByAssigneeUsername(String username);

    List<Task> findByProjectIdAndAssigneeUsername(Long projectId, String username);

    @Query("select t from Task t where t.project.id = :projectId and (lower(t.title) like lower(concat('%', :term, '%')) or lower(t.description) like lower(concat('%', :term, '%')))")
    List<Task> searchInProject(@Param("projectId") Long projectId, @Param("term") String term);
}
