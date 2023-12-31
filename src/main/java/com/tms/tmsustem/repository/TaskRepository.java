package com.tms.tmsustem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tms.tmsustem.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	// Inside TaskRepository interface
	List<Task> findByUserId(Long userId);
	
	List<Task> findByUserIdAndPriority(Long userId, Task.Priority priority);

    List<Task> findByUserIdAndStatus(Long userId, Task.Status status);
    
    //search by keyWord
    
    List<Task> findByUserIdAndTitleContainingOrDescriptionContainingIgnoreCase(
            Long userId, String titleKeyword, String descriptionKeyword);
    
    @Query("SELECT t FROM Task t WHERE t.user.username = :username " +
            "AND t.dueDate BETWEEN :startOfDay AND :endOfDay")
     List<Task> findTasksWithApproachingDueDatesForUser(
             @Param("username") String username,
             @Param("startOfDay") LocalDateTime startOfDay,
             @Param("endOfDay") LocalDateTime endOfDay);

}
